/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.response;

/**
 * Enum represents http response status code
 */
public enum ResponseStatusCode {
    OK(200),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    ResponseStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
