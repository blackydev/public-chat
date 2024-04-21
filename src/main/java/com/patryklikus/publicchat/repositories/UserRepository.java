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
                email VARCHAR(255) NOT NULL UNIQUE,
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
    public User findById(Long id) {
        return null;
    }

    @Override
    public User save(User user) {
        try {
            return user.getId() == null ? create(user) : update(user);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(User user) throws SQLException {
        String query = String.format("DELETE FROM users WHERE id = %s;", user.getId());
        try (Statement statement = postgresClient.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    private User create(User user) throws SQLException {
        String query = String.format(
                "INSERT INTO users (email, username, password, isAdmin) VALUES ('%s', '%s', '%s', '%b') RETURNING ID;",
                user.getEmail(), user.getUsername(), user.getPassword(), user.isAdmin()
        );
        try (Statement statement = postgresClient.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            user.setId(rs.getLong(1));
            return user;
        }
    }

    private User update(User user) throws SQLException {
        String query = String.format(
                "UPDATE users SET email = %s, username = %s , password = %s, isAdmin = %b WHERE id = %s;",
                user.getEmail(), user.getUsername(), user.getPassword(), user.isAdmin(), user.getId()
        );
        try (Statement statement = postgresClient.createStatement()) {
            return statement.executeUpdate(query) == 0 ? null : user;
        }
    }
}
