/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models;

import java.time.LocalDateTime;

public class Message implements Idable {
    private final User author;
    private final String content;
    private Long id;
    private LocalDateTime timestamp;

    Message(Long id, User author, String content, LocalDateTime timestamp) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
