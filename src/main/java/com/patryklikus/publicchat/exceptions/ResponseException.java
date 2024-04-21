/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.exceptions;

import com.patryklikus.publicchat.https.response.ResponseStatusCode;

public class ResponseException extends RuntimeException {
    private final ResponseStatusCode statusCode;

    public ResponseException(ResponseStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ResponseStatusCode getStatusCode() {
        return statusCode;
    }
}
