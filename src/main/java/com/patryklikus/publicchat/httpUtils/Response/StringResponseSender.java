package com.patryklikus.publicchat.httpUtils.Response;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class StringResponseSender implements ResponseSender {
    public void sendUnsafe(HttpExchange exchange, Response response) throws IOException {
        exchange.sendResponseHeaders(response.code().getCode(), response.body().length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.body().getBytes());
        os.close();
    }
}
