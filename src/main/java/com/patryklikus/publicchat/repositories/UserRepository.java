package com.patryklikus.publicchat.repositories;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.User;

public class UserRepository {
    private final PostgresClient postgresClient;

    public UserRepository(PostgresClient postgresClient) {
        this.postgresClient = postgresClient;
    }

    public User create(User user) {
        String query = String.format(
                "INSERT INTO users (email, username, password, isadmin) VALUES ('%s', '%s', '%s', '%s');",
                user.getEmail(), user.getUsername(), user.getPassword(), user.isAdmin()
        );
        System.out.println(" create : " + postgresClient.query(query));
        return null;
    }
}
