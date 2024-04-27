/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine;

import com.patryklikus.publicchat.https.annotations.Authenticated;
import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.services.AuthService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.function.Function;
import java.util.logging.Logger;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.*;

/**
 * Handles requests of one endpoint. It handles multiple types of requestMethods like GET, POST, PUT, and DELETE.
 * It handles {@link Authenticated} annotation.
 */
public class EndpointRequestHandler implements HttpHandler {
    private static final Logger LOG = Logger.getLogger(EndpointRequestHandler.class.getName());
    private final StringResponseSender responseSender;
    private final AuthService authService;
    private Function<Request, Response> getHandler;
    private Function<Request, Response> postHandler;
    private Function<Request, Response> putHandler;
    private Function<Request, Response> deleteHandler;

    public EndpointRequestHandler(StringResponseSender responseSender, AuthService authService) {
        this.responseSender = responseSender;
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            LOG.info("Request " + exchange.getRequestMethod() + " " + exchange.getRequestURI());
            Function<Request, Response> methodHandler = chooseHandler(exchange);
            Response response = getResponse(exchange, methodHandler);
            responseSender.send(exchange, response);
        } catch (RuntimeException e) {
            LOG.warning("Unexpected method handler exception: " + e.getStackTrace());
        }
    }

    private Response getResponse(HttpExchange exchange, Function<Request, Response> methodHandler) {
        if (methodHandler == null) {
            return new Response(BAD_REQUEST, "This website is unavailable");
        }
        Authentication authentication = authService.authenticate(exchange);
        Class<?> handlerClass = methodHandler.getClass();
        if (handlerClass.isAnnotationPresent(Authenticated.class)) {
            Authenticated authenticated = handlerClass.getAnnotation(Authenticated.class);
            if (authentication == null) {
                return new Response(UNAUTHORIZED);
            }
            if (authenticated.admin() && !authentication.isAdmin()) {
                return new Response(FORBIDDEN);
            }
        }
        Request request = Request.create(exchange, authentication);
        return applyHandler(methodHandler, request);
    }


    private Response applyHandler(Function<Request, Response> methodHandler, Request request) {
        try {
            return methodHandler.apply(request);
        } catch (RuntimeException e) {
            if (e.getCause().getCause() instanceof ResponseException re) {
                return new Response(re.getStatusCode(), re.getMessage());
            } else {
                return new Response(INTERNAL_SERVER_ERROR, "Unexpected server error");
            }
        }
    }

    private Function<Request, Response> chooseHandler(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        return switch (method) {
            case "GET" -> getHandler;
            case "POST" -> postHandler;
            case "PUT" -> putHandler;
            case "DELETE" -> deleteHandler;
            default -> null;
        };
    }

    public void setGetHandler(Function<Request, Response> getHandler) {
        this.getHandler = getHandler;
    }

    public void setPostHandler(Function<Request, Response> postHandler) {
        this.postHandler = postHandler;
    }

    public void setPutHandler(Function<Request, Response> putHandler) {
        this.putHandler = putHandler;
    }

    public void setDeleteHandler(Function<Request, Response> deleteHandler) {
        this.deleteHandler = deleteHandler;
    }
}
