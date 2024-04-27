/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository implements Repository<User> {
    private final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                isadmin BOOLEAN DEFAULT FALSE NOT NULL
            );
            """;
    private final PostgresClient postgresClient;

    public UserRepository(PostgresClient postgresClient) {
        this.postgresClient = postgresClient;
    }

    public void createTable() {
        try (Statement statement = postgresClient.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_QUERY);
        } catch (SQLException ignore) {
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
    public void remove(User user) {
        String query = String.format("DELETE FROM users WHERE id = %s;", user.getId());
        try (Statement stmt = postgresClient.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User create(User user) {
        String query = String.format(
                "INSERT INTO users (username, password, isAdmin) VALUES ('%s', '%s', '%b') RETURNING ID;",
                user.getUsername(), user.getPassword(), user.isAdmin()
        );
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            user.setId(rs.getLong("id"));
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User update(User user) {
        String query = String.format(
                "UPDATE users SET username = %s , password = %s, isAdmin = %b WHERE id = %s;",
                user.getUsername(), user.getPassword(), user.isAdmin(), user.getId()
        );
        try (Statement stmt = postgresClient.createStatement()) {
            return stmt.executeUpdate(query) == 0 ? null : user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
