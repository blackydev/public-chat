package com.patryklikus.publicchat.httpUtils.Response;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface ResponseSender {
    /**
     * Sends response to client. Closes HttpExchange.
     */
    default void send(HttpExchange exchange, Response response) {
        try {
            sendUnsafe(exchange, response);
        } catch (IOException e) {
            System.out.println("Can't send response to client: " + e.getCause());
        }
    }

    void sendUnsafe(HttpExchange exchange, Response response) throws IOException;
}
