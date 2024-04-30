/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.*;

import com.patryklikus.publicchat.https.annotations.Authenticated;
import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.EndpointMethod;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.services.AuthService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Handles requests of one endpoint. It handles multiple types of requestMethods like GET, POST, PUT, and DELETE.
 * It handles {@link Authenticated} annotation.
 */
public class EndpointHandler implements HttpHandler {
    private static final Logger LOG = Logger.getLogger(EndpointHandler.class.getName());
    private final StringResponseSender responseSender;
    private final AuthService authService;
    private EndpointMethod<?> getHandler;
    private EndpointMethod<?> postHandler;
    private EndpointMethod<?> putHandler;
    private EndpointMethod<?> deleteHandler;

    public EndpointHandler(StringResponseSender responseSender, AuthService authService) {
        this.responseSender = responseSender;
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            LOG.info("Request " + exchange.getRequestMethod() + " " + exchange.getRequestURI());
            EndpointMethod<?> methodHandler = chooseHandler(exchange);
            Response response = getResponse(exchange, methodHandler);
            responseSender.send(exchange, response);
        } catch (RuntimeException e) {
            LOG.warning("Unexpected method handler exception: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private Response getResponse(HttpExchange exchange, EndpointMethod<?> method) {
        if (method == null) {
            return new Response(BAD_REQUEST, "This website is unavailable");
        }
        Authentication authentication = authService.authenticate(exchange.getRequestHeaders());
        if (method.isAnnotationPresent(Authenticated.class)) {
            if (authentication == null) {
                return new Response(UNAUTHORIZED);
            }
            if (method.getAnnotation(Authenticated.class).admin() && !authentication.isAdmin()) {
                return new Response(FORBIDDEN);
            }
        }
        Request request = Request.create(exchange, authentication);
        return method.apply(request);
    }

    private EndpointMethod<?> chooseHandler(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        return switch (method) {
            case "GET" -> getHandler;
            case "POST" -> postHandler;
            case "PUT" -> putHandler;
            case "DELETE" -> deleteHandler;
            default -> null;
        };
    }

    public void setGetHandler(EndpointMethod<?> getHandler) {
        this.getHandler = getHandler;
    }

    public void setPostHandler(EndpointMethod<?> postHandler) {
        this.postHandler = postHandler;
    }

    public void setPutHandler(EndpointMethod<?> putHandler) {
        this.putHandler = putHandler;
    }

    public void setDeleteHandler(EndpointMethod<?> deleteHandler) {
        this.deleteHandler = deleteHandler;
    }
}
