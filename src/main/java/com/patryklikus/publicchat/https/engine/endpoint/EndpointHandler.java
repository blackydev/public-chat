/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine.endpoint;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;
import static com.patryklikus.publicchat.https.models.ResponseStatusCode.INTERNAL_SERVER_ERROR;

import com.patryklikus.publicchat.https.engine.ResponseSender;
import com.patryklikus.publicchat.https.engine.endpoint.method.EndpointMethodChooser;
import com.patryklikus.publicchat.https.models.EndpointMethod;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.https.models.ResponseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.logging.Logger;

/**
 * Handles requests of single endpoint.
 */
public class EndpointHandler implements HttpHandler {
    private static final Logger LOG = Logger.getLogger(EndpointHandler.class.getName());
    private final EndpointMethodChooser endpointMethodChooser;
    private final RequestCreator requestCreator;
    private final ResponseSender responseSender;
    private final PrivilegeResolver privilegeResolver;

    public EndpointHandler(EndpointMethodChooser endpointMethodChooser, RequestCreator requestCreator,
                           ResponseSender responseSender, PrivilegeResolver privilegeResolver) {
        this.endpointMethodChooser = endpointMethodChooser;
        this.requestCreator = requestCreator;
        this.responseSender = responseSender;
        this.privilegeResolver = privilegeResolver;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Response response;
        try {
            response = handleCorrectRequest(exchange);
        } catch (RuntimeException e) {
            if (e.getCause().getCause() instanceof ResponseException re) {
                response = new Response(re.getStatusCode(), re.getMessage());
            } else {
                LOG.warning("Unexpected method handler exception: " + e.getCause().getCause().getMessage());
                response = new Response(INTERNAL_SERVER_ERROR, "Unexpected server error");
            }
        }
        responseSender.send(exchange, response);
    }

    private Response handleCorrectRequest(HttpExchange exchange) {
        Request request = requestCreator.apply(exchange);
        LOG.info("Request " + request.getRequestMethod() + " " + request.getRequestURI());
        EndpointMethod endpointMethod = endpointMethodChooser.apply(request);
        if (endpointMethod == null) {
            return new Response(BAD_REQUEST);
        }
        privilegeResolver.test(endpointMethod, request);
        return endpointMethod.apply(request);
    }
}
