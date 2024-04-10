/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.engine.https.request;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Represents Http Request.
 */
public class Request implements com.sun.net.httpserver.Request {
    private final URI uri;
    private final String method;
    private final Headers headers;
    private final String body;

    public static Request create(HttpExchange exchange) {
        InputStreamReader inputStreamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String body = bufferedReader.lines().collect(Collectors.joining("\n"));
        return new Request(exchange.getRequestURI(), exchange.getRequestMethod(), exchange.getRequestHeaders(), body);
    }

    Request(URI uri, String method, Headers headers, String body) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.body = body;
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

    public String getRequestBody() {
        return body;
    }
}
