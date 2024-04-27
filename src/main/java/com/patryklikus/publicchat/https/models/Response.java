/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.models;

import static com.patryklikus.publicchat.https.models.ResponseBodyFormat.STRING;
import static com.patryklikus.publicchat.https.models.ResponseStatusCode.OK;

import java.util.Objects;

/**
 * Represents response which controllers methods should return.
 */
public record Response(
        ResponseStatusCode code,
        String body,
        ResponseBodyFormat format
) {
    public Response(ResponseStatusCode code, String body, ResponseBodyFormat format) {
        this.code = code;
        this.body = Objects.requireNonNullElse(body, "");
        this.format = format;
    }

    public Response(ResponseStatusCode code, String body) {
        this(code, body, STRING);
    }

    public Response(String body) {
        this(OK, body, STRING);
    }

    public Response(ResponseStatusCode code) {
        this(code, code.name(), STRING);
    }
}
