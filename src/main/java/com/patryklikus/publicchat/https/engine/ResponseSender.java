/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine;

import com.patryklikus.publicchat.https.models.Response;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Logger;

public interface ResponseSender {
    Logger LOG = Logger.getLogger(ResponseSender.class.getName());

    /**
     * Sends response to client. Closes HttpExchange.
     */
    default void send(HttpExchange exchange, Response response) {
        try {
            sendUnsafe(exchange, response);
        } catch (IOException e) {
            LOG.info("Can't send response to client: " + e.getCause());
        }
    }

    /**
     * Sends response to client. Closes HttpExchange.
     */
    void sendUnsafe(HttpExchange exchange, Response response) throws IOException;
}
