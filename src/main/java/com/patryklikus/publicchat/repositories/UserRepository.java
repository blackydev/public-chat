package com.patryklikus.publicchat.repositories;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class UserRepository {
    private static final Logger LOG = Logger.getLogger(UserRepository.class.getName());
    private final PostgresClient postgresClient;

    public UserRepository(PostgresClient postgresClient) {
        this.postgresClient = postgresClient;
    }

    public User create(User user) {
        String query = String.format(
                "INSERT INTO users (email, username, password, isadmin) VALUES ('%s', '%s', '%s', '%s');",
                user.getEmail(), user.getUsername(), user.getPassword(), user.isAdmin()
        );
        Statement statement = postgresClient.createStatement(); // todo remember about closing

        try {
            int updated = statement.executeUpdate(query);
            if (updated == 0) {
                return null;
            }
            query = String.format(
                    "SELECT * FROM users WHERE email = %s AND username = %s;",
                    user.getEmail(), user.getUsername()
            );
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    user.setId(generatedKeys.getLong(1));
                else
                    return null;
            }

            statement.close();
            return user;
        } catch (SQLException e) {
            LOG.warning("Exception during creating user in database: " + e.getMessage());
        }
        return null;
    }
}
