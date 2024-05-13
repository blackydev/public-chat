/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
        String query = """
                CREATE TABLE IF NOT EXISTS messages (
                    id serial primary key,
                    author_id integer not null references "users",
                    content text not null,
                    timestamp timestamp default CURRENT_TIMESTAMP not null
                );
                """;
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.executeUpdate();
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
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
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
                JOIN users u ON m.author_id = u.id
                WHERE m.id >= ? AND m.id <= ?;
                """;
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setLong(1, idFrom);
            statement.setLong(2, idTo);
            ResultSet rs = statement.executeQuery();
            List<Message> messages = new LinkedList<>();
            while (rs.next()) {
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
        String query = "DELETE FROM messages WHERE id = ?;";
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
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
        String query = "INSERT INTO message-box.css (author_id, content) VALUES (?, ?) RETURNING ID;";
        try (PreparedStatement statement = postgresClient.prepareStatement(query)) {
            statement.setLong(1, message.getAuthor().getId());
            statement.setString(2, message.getContent());
            ResultSet rs = statement.executeQuery();
            rs.next();
            message.setId(rs.getLong("id"));
            message.setTimestamp(LocalDateTime.now());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
