/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.engine.https.response;

/**
 * Enum represents http response status code
 */
public enum ResponseStatusCode {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERVAL_SERVER_ERROR(500);

    private final int code;

    ResponseStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
