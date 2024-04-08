package com.patryklikus.publicchat.httpUtils.Response;

public record Response(
        ResponseStatusCode code,
        String body
) {
}
