/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.patryklikus.publicchat.models.UserBuilder.anUser;

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

    public User findByUsername(String username) {
        String query = "SELECT id, isAdmin, password FROM users WHERE username = ? LIMIT 1";
        try (PreparedStatement stmt = postgresClient.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
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
        try (PreparedStatement stmt = postgresClient.prepareStatement(query)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void create(User user) {
        String query = "INSERT INTO users (username, password, isAdmin) VALUES (?, ?, ?) RETURNING ID;";
        try (PreparedStatement stmt = postgresClient.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setBoolean(3, user.isAdmin());
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            user.setId(rs.getLong("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(User user) {
        String query = "UPDATE users SET username = ? , password = ?, isAdmin = ? WHERE id = ?;";
        try (PreparedStatement stmt = postgresClient.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setBoolean(3, user.isAdmin());
            stmt.setLong(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
