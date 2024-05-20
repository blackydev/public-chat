/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https;

import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.engine.DefaultResponseSender;
import com.patryklikus.publicchat.https.engine.ResponseSender;
import com.patryklikus.publicchat.https.engine.endpoint.EndpointHandler;
import com.patryklikus.publicchat.https.engine.endpoint.PrivilegeResolver;
import com.patryklikus.publicchat.https.engine.endpoint.RequestCreator;
import com.patryklikus.publicchat.https.engine.endpoint.method.EndpointMethodChooser;
import com.patryklikus.publicchat.https.models.EndpointMethod;
import com.patryklikus.publicchat.https.models.RequestMethod;
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
    private final ResponseSender responseSender;
    private final RequestCreator requestCreator;
    private final PrivilegeResolver privilegeResolver;

    private final Map<String, EndpointMethodChooser> endpointMethodChoosers;

    public RequestHandlersManager(HttpServer server, AuthService authService) {
        this.server = server;
        this.authService = authService;
        responseSender = new DefaultResponseSender();
        endpointMethodChoosers = new HashMap<>();
        privilegeResolver = new PrivilegeResolver();
        requestCreator = new RequestCreator(authService);
    }

    public void init() {
        endpointMethodChoosers.forEach(this::createEndpoint);
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
            RequestMethod methodName = null;
            String endpoint = null;
            EndpointMethod endpointMethod = new EndpointMethod(controller, method);

            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            if (getMapping != null) {
                methodName = RequestMethod.GET;
                endpoint = basePath + getMapping.path();
            }

            PostMapping postMapping = method.getAnnotation(PostMapping.class);
            if (postMapping != null) {
                methodName = RequestMethod.POST;
                endpoint = basePath + postMapping.path();
            }

            PutMapping putMapping = method.getAnnotation(PutMapping.class);
            if (putMapping != null) {
                methodName = RequestMethod.PUT;
                endpoint = basePath + putMapping.path();
            }

            DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
            if (deleteMapping != null) {
                methodName = RequestMethod.DELETE;
                endpoint = basePath + deleteMapping.path();
            }

            if (methodName != null) {
                getEndpointHandler(endpoint).addMethod(methodName, endpointMethod);
                LOG.info(String.format(
                        "Create handler for %s method. Endpoint: %s handling method: %s",
                        methodName, endpoint, controllerClass.getName() + "#" + method.getName()
                ));
            }
        }
    }

    private EndpointMethodChooser getEndpointHandler(String endpoint) {
        if (endpoint.endsWith("/"))
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        if (endpoint.isEmpty())
            endpoint = "/";
        return endpointMethodChoosers.computeIfAbsent(endpoint, k -> new EndpointMethodChooser());
    }

    private void createEndpoint(String endpoint, EndpointMethodChooser chooser) {
        EndpointHandler endpointHandler = new EndpointHandler(chooser, requestCreator, responseSender, privilegeResolver);
        server.createContext(endpoint, endpointHandler);
    }
}
