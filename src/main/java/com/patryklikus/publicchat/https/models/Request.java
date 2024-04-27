/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.models;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Represents Http Request in controllers methods.
 */
public class Request implements com.sun.net.httpserver.Request {
    private final URI uri;
    private final String method;
    private final Headers headers;
    private final Authentication authentication;
    private final String body;

    Request(URI uri, String method, Headers headers, Authentication authentication, String body) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.authentication = authentication;
        this.body = body;
    }

    public static Request create(HttpExchange exchange, Authentication authentication) {
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String body = bufferedReader.lines().collect(Collectors.joining("\n"));
        return new Request(exchange.getRequestURI(), exchange.getRequestMethod(), exchange.getRequestHeaders(), authentication, body);
    }

    @Override
    public URI getRequestURI() {
        return uri;
    }

    @Override
    public String getRequestMethod() {
        return method;
    }

    @Override
    public Headers getRequestHeaders() {
        return headers;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public String getRequestBody() {
        return body;
    }
}
