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
    private static final String CREATE_HANDLER_LOG = "Create handler for %s method. Endpoint: %s handling method: %s";
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
            var endpointMethod = new EndpointMethod<>(controller, method);
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                EndpointHandler endpointHandler = getEndpointHandler(basePath, getMapping.path());
                endpointHandler.setGetHandler(endpointMethod);
                LOG.fine(String.format(CREATE_HANDLER_LOG, "GET", basePath + getMapping.path(), controllerClass.getName() + "#" + method.getName()));
            }

            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if (postMapping != null) {
                EndpointHandler endpointHandler = getEndpointHandler(basePath, postMapping.path());
                endpointHandler.setPostHandler(endpointMethod);
                LOG.fine(String.format(CREATE_HANDLER_LOG, "GET", basePath + postMapping.path(), controllerClass.getName() + "#" + method.getName()));
            }

            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            if (putMapping != null) {
                EndpointHandler endpointHandler = getEndpointHandler(basePath, putMapping.path());
                endpointHandler.setPutHandler(endpointMethod);
                LOG.fine(String.format(CREATE_HANDLER_LOG, "GET", basePath + putMapping.path(), controllerClass.getName() + "#" + method.getName()));
            }

            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            if (deleteMapping != null) {
                EndpointHandler endpointHandler = getEndpointHandler(basePath, deleteMapping.path());
                endpointHandler.setDeleteHandler(endpointMethod);
                LOG.fine(String.format(CREATE_HANDLER_LOG, "GET", basePath + deleteMapping.path(), controllerClass.getName() + "#" + method.getName()));
            }
        }
    }

    private EndpointHandler getEndpointHandler(String basePath, String path) {
        String endpoint = basePath + path;
        if (endpoint.isEmpty())
            endpoint = "/";
        return endpointHandlers.computeIfAbsent(endpoint, k -> new EndpointHandler(stringResponseSender, authService));
    }
}
