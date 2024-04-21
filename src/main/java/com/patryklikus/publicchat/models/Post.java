/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private final Long authorId;
    private final String content;
    private final LocalDateTime timestamp;

    Post(Long id, Long authorId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
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
}
