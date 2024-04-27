/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.models;

import com.patryklikus.publicchat.models.User;

/**
 * Represents an authenticated user.
 */
public record Authentication(long userId, boolean isAdmin) {
    public Authentication(User user) {
        this(user.getId(), user.isAdmin());
    }
}
