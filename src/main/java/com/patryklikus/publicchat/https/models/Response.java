/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.models;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.OK;

import java.util.Objects;

/**
 * Represents response which controllers methods should return.
 */
public record Response(
        ResponseStatusCode code,
        String body
) {
    public Response(ResponseStatusCode code, String body) {
        this.code = code;
        this.body = Objects.requireNonNullElse(body, "");
    }

    public Response(String body) {
        this(OK, body);
    }

    public Response(ResponseStatusCode statusCode) {
        this(statusCode, null);
    }
}
