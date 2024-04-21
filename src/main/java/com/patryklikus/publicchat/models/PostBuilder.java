/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.models;

import java.time.LocalDateTime;

public final class PostBuilder {
    private Long id;
    private Long authorId;
    private String content;
    private LocalDateTime timestamp;

    private PostBuilder() {
    }

    public static PostBuilder aPost() {
        return new PostBuilder();
    }

    public PostBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PostBuilder withAuthorId(Long authorId) {
        this.authorId = authorId;
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

    public Post build() {
        return new Post(id, authorId, content, timestamp);
    }
}
