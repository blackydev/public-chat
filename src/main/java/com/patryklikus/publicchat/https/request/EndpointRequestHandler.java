/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.request;

import com.patryklikus.publicchat.https.response.Response;
import com.patryklikus.publicchat.https.response.ResponseStatusCode;
import com.patryklikus.publicchat.https.response.StringResponseSender;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Handles requests of one endpoint. It handles multiple types of requestMethods like get, post, put, and delete.
 */
public class EndpointRequestHandler implements HttpHandler {
    private static final Logger LOG = Logger.getLogger(EndpointRequestHandler.class.getName());
    private final StringResponseSender responseSender;
    private Function<Request, Response> getHandler;
    private Function<Request, Response> postHandler;
    private Function<Request, Response> putMethod;
    private Function<Request, Response> deleteMethod;

    public EndpointRequestHandler(StringResponseSender responseSender) {
        this.responseSender = responseSender;
    }

    @Override
    public void handle(HttpExchange exchange) {
        LOG.info("Request " + exchange.getRequestMethod()  + " " + exchange.getRequestURI());
        String method = exchange.getRequestMethod();
        Request request = Request.create(exchange);
        Function<Request, Response> methodHandler = null;
        switch (method) {
            case "GET" -> methodHandler = getHandler;
            case "POST" -> methodHandler = postHandler;
            case "PUT" -> methodHandler = putMethod;
            case "DELETE" -> methodHandler = deleteMethod;
        }
        if(methodHandler == null) {
            Response response = new Response(ResponseStatusCode.BAD_REQUEST, "This website is unavailable");
            responseSender.send(exchange, response); // todo add website sender
        } else {
            try {
                Response response = methodHandler.apply(request);
                responseSender.send(exchange, response);
            } catch (RuntimeException e) {
                LOG.info(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public void setGetHandler(Function<Request, Response> getHandler) {
        this.getHandler = getHandler;
    }

    public void setPostHandler(Function<Request, Response> postHandler) {
        this.postHandler = postHandler;
    }

    public void setPutMethod(Function<Request, Response> putMethod) {
        this.putMethod = putMethod;
    }

    public void setDeleteMethod(Function<Request, Response> deleteMethod) {
        this.deleteMethod = deleteMethod;
    }
}
