/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;

import static com.patryklikus.publicchat.models.PostBuilder.aMessage;
import static com.patryklikus.publicchat.models.UserBuilder.anUser;

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

public class MessageRepository implements Repository<Message> {
    private final String CREATE_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS messages (
                id serial primary key,
                author_id integer not null references "users",
                content text not null,
                timestamp timestamp default CURRENT_TIMESTAMP not null
            );
            """;
    private final PostgresClient postgresClient;

    public MessageRepository(PostgresClient postgresClient) {
        this.postgresClient = postgresClient;
    }

    public void createTable() {
        try (Statement statement = postgresClient.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_QUERY);
        } catch (SQLException ignore) {
        }
    }

    public List<Message> findMany(Long idFrom, Long idTo) {
        String query = String.format("SELECT m.id, m.content, m.timestamp, u.id AS author_id, u.username AS author_username FROM messages m WHERE id >= %s AND id <= %s JOIN users u ON m.author_id = u.id", idFrom, idTo);
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
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
            return Collections.emptyList();
        }
    }

    @Override
    public void save(Message message) {
        if (message.getId() != null) {
            throw new RuntimeException("Message s is immutable!");
        }
        create(message);
    }

    @Override
    public void remove(Message message) {
        String query = String.format("DELETE FROM messages WHERE id = %s;", message.getId());
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
                "INSERT INTO messages (author_id, content) VALUES ('%s', '%s') RETURNING ID;",
                message.getAuthor().getId(), message.getContent()
        );
        try (Statement stmt = postgresClient.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            message.setId(rs.getLong("id"));
            LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
            message.setTimestamp(timestamp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
