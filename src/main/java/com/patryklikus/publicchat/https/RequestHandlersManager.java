/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https;

import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.engine.EndpointHandler;
import com.patryklikus.publicchat.https.engine.StringResponseSender;
import com.patryklikus.publicchat.https.models.EndpointMethod;
import com.patryklikus.publicchat.services.AuthService;
import com.sun.net.httpserver.HttpServer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * It creates {@link EndpointHandler}s from controllers methods.
 */
public class RequestHandlersManager {
    private static final Logger LOG = Logger.getLogger(RequestHandlersManager.class.getName());
    private final HttpServer server;
    private final AuthService authService;
    private final StringResponseSender stringResponseSender;

    private final Map<String, EndpointHandler> endpointHandlers;

    public RequestHandlersManager(HttpServer server, AuthService authService) {
        this.server = server;
        this.authService = authService;
        stringResponseSender = new StringResponseSender();
        endpointHandlers = new HashMap<>();
    }

    public void init() {
        endpointHandlers.forEach(server::createContext);
    }

    public void addControllers(Object... controllers) {
        Arrays.stream(controllers).forEach(this::addController);
    }

    private void addController(Object controller) {
        Class<?> controllerClass = controller.getClass();
        LOG.info("Adding controller to server: " + controllerClass.getName());
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        String basePath = requestMapping == null
                ? ""
                : requestMapping.path();

        for (Method method : controllerClass.getMethods()) {
            String methodName = null, endpoint = null;
            EndpointMethod<?> endpointMethod = new EndpointMethod<>(controller, method);

            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                methodName = "GET";
                endpoint = basePath + getMapping.path();
                getEndpointHandler(endpoint).setGetHandler(endpointMethod);
            }

            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if (postMapping != null) {
                methodName = "POST";
                endpoint = basePath + postMapping.path();
                getEndpointHandler(endpoint).setPostHandler(endpointMethod);
            }

            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            if (putMapping != null) {
                methodName = "PUT";
                endpoint = basePath + putMapping.path();
                getEndpointHandler(endpoint).setPutHandler(endpointMethod);
            }

            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            if (deleteMapping != null) {
                methodName = "DELETE";
                endpoint = basePath + deleteMapping.path();
                getEndpointHandler(endpoint).setDeleteHandler(endpointMethod);
            }

            if (methodName != null) {
                LOG.fine(String.format(
                        "Create handler for %s method. Endpoint: %s handling method: %s",
                        methodName, endpoint, controllerClass.getName() + "#" + method.getName()
                ));
            }
        }
    }

    private EndpointHandler getEndpointHandler(String endpoint) {
        if (endpoint.endsWith("/"))
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        if (endpoint.isEmpty())
            endpoint = "/";
        return endpointHandlers.computeIfAbsent(endpoint, k -> new EndpointHandler(stringResponseSender, authService));
    }
}
