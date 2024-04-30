/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;

import static com.patryklikus.publicchat.models.UserBuilder.anUser;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository implements Repository<User> {
    private final PostgresClient postgresClient;

    public UserRepository(PostgresClient postgresClient) {
        this.postgresClient = postgresClient;
    }

    public void createTable() {
        try (Statement statement = postgresClient.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        isadmin BOOLEAN DEFAULT FALSE NOT NULL
                    );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        String query = String.format("SELECT users WHERE username = %s LIMIT 1", username);
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                long id = rs.getLong("id");
                boolean isAdmin = rs.getBoolean("isAdmin");
                return anUser().withId(id)
                        .withUsername(username)
                        .withIsAdmin(isAdmin)
                        .build();
            }
            return null;
        } catch (SQLException e) {
            return null;
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
        String query = String.format("DELETE FROM users WHERE id = %s;", id);
        try (Statement stmt = postgresClient.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void create(User user) {
        String query = String.format(
                "INSERT INTO users (username, password, isAdmin) VALUES ('%s', '%s', '%b') RETURNING ID;",
                user.getUsername(), user.getPassword(), user.isAdmin()
        );
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            user.setId(rs.getLong("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(User user) {
        String query = String.format(
                "UPDATE users SET username = %s , password = %s, isAdmin = %b WHERE id = %s;",
                user.getUsername(), user.getPassword(), user.isAdmin(), user.getId()
        );
        try (Statement stmt = postgresClient.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
