/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.clients;

import java.sql.*;
import java.util.List;
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

    public void initDatabase() {
        LOG.info("Initializing database...");
        List.of("""
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    isadmin BOOLEAN DEFAULT FALSE NOT NULL
                );
                """, """
                CREATE TABLE IF NOT EXISTS posts (
                    id serial primary key,
                    author_id integer not null references "users",
                    content text not null,
                    timestamp timestamp default CURRENT_TIMESTAMP not null
                );
                """
        ).forEach(this::query);
    }

    /**
     * Create statement and executes query. It's important to close statement.
     */
    public ResultSet query(String query) {
        try {
            Statement statement = connection.createStatement(); // todo remember about closing
            return statement.executeQuery(query);
        } catch (SQLException e) {
            LOG.warning("Exception during quering a database: " + e.getMessage());
            return null;
        }
    }

    public void close() throws SQLException {
        connection.close();
        LOG.info("Application has been disconnected from database");
    }
}
