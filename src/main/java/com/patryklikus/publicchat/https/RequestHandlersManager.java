/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https;

import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.engine.EndpointRequestHandler;
import com.patryklikus.publicchat.https.engine.StringResponseSender;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.sun.net.httpserver.HttpServer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

public class RequestHandlersManager {
    private static final Logger LOG = Logger.getLogger(RequestHandlersManager.class.getName());
    private final HttpServer server;
    private final StringResponseSender stringResponseSender;

    private final Map<String, EndpointRequestHandler> endpointHandlers;

    public RequestHandlersManager(HttpServer server) {
        this.server = server;
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
            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                EndpointRequestHandler endpointHandler = getEndpointHandler(basePath, getMapping.path());
                endpointHandler.setGetHandler(methodToHandlerFunction(controller, method));
                LOG.info("Create handler for GET request method. Endpoint: " + basePath + getMapping.path() + " handling method: " + controllerClass.getName() + "." + method.getName() + "()");
            }

            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if (postMapping != null) {
                EndpointRequestHandler endpointHandler = getEndpointHandler(basePath, postMapping.path());
                endpointHandler.setPostHandler(methodToHandlerFunction(controller, method));
                LOG.info("Create handler for POST request method. Endpoint: " + basePath + postMapping.path() + " handling method: " + controllerClass.getName() + "." + method.getName() + "()");
            }

            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            if (putMapping != null) {
                EndpointRequestHandler endpointHandler = getEndpointHandler(basePath, putMapping.path());
                endpointHandler.setPutMethod(methodToHandlerFunction(controller, method));
                LOG.info("Create handler for PUT request method: " + basePath + putMapping.path() + " handling method: " + controllerClass.getName() + "." + method.getName() + "()");
            }

            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            if (deleteMapping != null) {
                EndpointRequestHandler endpointHandler = getEndpointHandler(basePath, deleteMapping.path());
                endpointHandler.setDeleteMethod(methodToHandlerFunction(controller, method));
                LOG.info("Create handler for DELETE request method: " + basePath + deleteMapping.path() + " handling method: " + controllerClass.getName() + "." + method.getName() + "()");
            }
        }
    }

    private EndpointRequestHandler getEndpointHandler(String basePath, String path) {
        String endpoint = basePath + path;
        if (endpoint.isEmpty())
            endpoint = "/";
        return endpointHandlers.computeIfAbsent(endpoint, k -> new EndpointRequestHandler(stringResponseSender));
    }

    private Function<Request, Response> methodToHandlerFunction(Object object, Method method) {
        return request -> {
            try {
                return (Response) method.invoke(object, request);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
