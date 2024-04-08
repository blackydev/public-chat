package com.patryklikus.publicchat.httpUtils.Response;

/**
 * Enum represents http response status code
 */
public enum ResponseStatusCode {
    OK(200),
    BAD_REQUEST(400),
    INTERVAL_SERVER_ERROR(500);

    private final int code;

    ResponseStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
