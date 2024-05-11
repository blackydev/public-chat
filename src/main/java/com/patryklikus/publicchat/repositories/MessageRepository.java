/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.patryklikus.publicchat.models.MessageBuilder.aMessage;
import static com.patryklikus.publicchat.models.UserBuilder.anUser;

public class MessageRepository implements Repository<Message> {
    private final PostgresClient postgresClient;

    public MessageRepository(PostgresClient postgresClient) {
        this.postgresClient = postgresClient;
    }

    public void createTable() {
        try (Statement statement = postgresClient.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS messages (
                        id serial primary key,
                        author_id integer not null references "users",
                        content text not null,
                        timestamp timestamp default CURRENT_TIMESTAMP not null
                    );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Message findLast() {
        String query = """
                SELECT m.id, m.content, m.timestamp, u.id AS author_id, u.username AS author_username
                FROM messages m
                JOIN users u ON m.author_id = u.id
                ORDER BY m.id DESC
                LIMIT 1;
                """;
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                long id = rs.getLong("id");
                String content = rs.getString("content");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                return aMessage().withId(id)
                        .withAuthor(getAuthor(rs))
                        .withContent(content)
                        .withTimestamp(timestamp.toLocalDateTime())
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> findMany(Long idFrom, Long idTo) {
        String query = """
                SELECT m.id, m.content, m.timestamp, u.id AS author_id, u.username AS author_username
                FROM messages m
                WHERE m.id >= %s AND m.id <= %s
                JOIN users u ON m.author_id = u.id;
                """;
        String formattedQuery = String.format(query, idFrom, idTo);
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(formattedQuery);
            List<Message> messages = new LinkedList<>();
            if (rs.next()) {
                long id = rs.getLong("id");
                String content = rs.getString("content");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                Message message = aMessage().withId(id)
                        .withAuthor(getAuthor(rs))
                        .withContent(content)
                        .withTimestamp(timestamp.toLocalDateTime())
                        .build();
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void save(Message message) {
        if (message.getId() != null) {
            throw new RuntimeException("Message is immutable!");
        }
        create(message);
    }

    @Override
    public void remove(long id) {
        String query = String.format("DELETE FROM messages WHERE id = %s;", id);
        try (Statement stmt = postgresClient.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User getAuthor(ResultSet rs) throws SQLException {
        long authorId = rs.getLong("author_id");
        String authorUsername = rs.getString("author_username");
        return anUser().withId(authorId).withUsername(authorUsername).build();
    }

    private void create(Message message) {
        String query = String.format(
                "INSERT INTO messages (author_id, content) VALUES (%s, '%s') RETURNING ID;",
                message.getAuthor().getId(), message.getContent()
        );
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            message.setId(rs.getLong("id"));
            message.setTimestamp(LocalDateTime.now());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
