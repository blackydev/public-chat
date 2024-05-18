/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine.endpoint;

import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.services.AuthService;
import com.sun.net.httpserver.HttpExchange;
import java.util.function.Function;

public class RequestCreator implements Function<HttpExchange, Request> {
    private final AuthService authService;

    public RequestCreator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Request apply(HttpExchange exchange) {
        Authentication authentication = authService.authenticate(exchange.getRequestHeaders());
        return Request.create(exchange, authentication);
    }
}
