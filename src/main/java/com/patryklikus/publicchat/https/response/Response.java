/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.response;

public record Response(
        ResponseStatusCode code,
        String body,
        ResponseBodyFormat format
) {
    public Response(ResponseStatusCode code, String body) {
        this(code, body, ResponseBodyFormat.STRING);
    }
}
