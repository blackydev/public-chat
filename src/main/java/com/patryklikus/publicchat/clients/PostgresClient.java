/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.clients;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PostgresClient {
    private static final Logger LOG = Logger.getLogger(PostgresClient.class.getName());
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public PostgresClient(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void connect() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        LOG.info("Connecting to postgreSQL...");
        connection = DriverManager.getConnection(url, user, password);
        LOG.info("Application has been connected to database");
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public void close() throws SQLException { // todo
        connection.close();
        LOG.info("Application has been disconnected from database");
    }
}
