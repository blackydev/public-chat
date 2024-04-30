/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine;

import com.patryklikus.publicchat.https.models.Response;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class StringResponseSender extends ResponseSender {
    protected void sendUnsafe(HttpExchange exchange, Response response) throws IOException {
        exchange.sendResponseHeaders(response.code().getCode(), response.body().length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.body().getBytes());
        os.close();
    }
}
