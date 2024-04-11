/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.clients;

import java.sql.*;

public class PostgresqlClient {
    private Connection connection;

    public void connect(String url, String user, String password) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // String url = "jdbc:postgresql://localhost:5432/db";
        // String user = "user";
        // String password = "password";
        connection = DriverManager.getConnection(url, user, password);
    }

    public ResultSet query(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            return null;
        }
    }

    public void close() throws SQLException {
        connection.close();
    }
}
