/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models;

import java.time.LocalDateTime;

public final class PostBuilder {
    private Long id;
    private User author;
    private String content;
    private LocalDateTime timestamp;

    private PostBuilder() {
    }

    public static PostBuilder aMessage() {
        return new PostBuilder();
    }

    public PostBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PostBuilder withAuthor(User author) {
        this.author = author;
        return this;
    }

    public PostBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public PostBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Message build() {
        return new Message(id, author, content, timestamp);
    }
}
