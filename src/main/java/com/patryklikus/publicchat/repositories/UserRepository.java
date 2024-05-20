/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;

import static com.patryklikus.publicchat.models.UserBuilder.anUser;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements Repository<User> {
    private final PostgresClient postgresClient;

    public UserRepository(PostgresClient postgresClient) {
        this.postgresClient = postgresClient;
    }

    public void createTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    isadmin BOOLEAN DEFAULT FALSE NOT NULL
                );
                """;
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(long userId) {
        String query = "SELECT username, isAdmin, password FROM users WHERE id = ? LIMIT 1";
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                boolean isAdmin = rs.getBoolean("isAdmin");
                String password = rs.getString("password");
                return anUser().withId(userId)
                        .withUsername(username)
                        .withIsAdmin(isAdmin)
                        .withPassword(password)
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        String query = "SELECT id, isAdmin, password FROM users WHERE username = ? LIMIT 1";
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                boolean isAdmin = rs.getBoolean("isAdmin");
                String password = rs.getString("password");
                return anUser().withId(id)
                        .withUsername(username)
                        .withIsAdmin(isAdmin)
                        .withPassword(password)
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        if (user.getId() == null) {
            create(user);
        } else {
            update(user);
        }
    }

    @Override
    public void remove(long id) {
        String query = "DELETE FROM users WHERE id = ?;";
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void create(User user) {
        String query = "INSERT INTO users (username, password, isAdmin) VALUES (?, ?, ?) RETURNING ID;";
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setBoolean(3, user.isAdmin());
            ResultSet rs = statement.executeQuery();
            rs.next();
            user.setId(rs.getLong("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(User user) {
        String query = "UPDATE users SET username = ? , password = ?, isAdmin = ? WHERE id = ?;";
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setBoolean(3, user.isAdmin());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
