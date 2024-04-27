/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models;

import java.time.LocalDateTime;

public final class MessageBuilder {
    private Long id;
    private User author;
    private String content;
    private LocalDateTime timestamp;

    private MessageBuilder() {
    }

    public static MessageBuilder aMessage() {
        return new MessageBuilder();
    }

    public MessageBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MessageBuilder withAuthor(User author) {
        this.author = author;
        return this;
    }

    public MessageBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public MessageBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Message build() {
        return new Message(id, author, content, timestamp);
    }
}
