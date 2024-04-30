/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine;

import com.patryklikus.publicchat.https.models.Response;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class ResponseSender {
    private final Logger logger = Logger.getLogger(getClass().toString());

    /**
     * Sends response to client. Closes HttpExchange.
     */
    void send(HttpExchange exchange, Response response) {
        try {
            sendUnsafe(exchange, response);
        } catch (IOException e) {
            logger.info("Can't send response to client: " + e.getCause());
        }
    }

    /**
     * Sends response to client. Closes HttpExchange.
     */
    protected abstract void sendUnsafe(HttpExchange exchange, Response response) throws IOException;
}
