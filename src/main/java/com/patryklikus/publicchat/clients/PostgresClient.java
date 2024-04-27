/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.clients;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class PostgresClient {
    private static final Logger LOG = Logger.getLogger(PostgresClient.class.getName());
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;

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

    public Statement createStatement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            LOG.warning("Exception during creating statement to postgreSQL database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void close() throws SQLException {
        connection.close();
        LOG.info("Application has been disconnected from database");
    }

    private void executeUpdate(String query) {
        try (Statement statement = createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException ignore) {
        }
    }
}
