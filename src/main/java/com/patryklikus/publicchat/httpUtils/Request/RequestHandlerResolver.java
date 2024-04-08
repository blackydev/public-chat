package com.patryklikus.publicchat.httpUtils.Request;

import com.patryklikus.publicchat.httpUtils.Request.mapping.GetMapping;
import com.patryklikus.publicchat.httpUtils.Request.mapping.RequestMapping;
import com.sun.net.httpserver.HttpServer;

import java.lang.reflect.Method;

public class RequestHandlerResolver { // todo change name
    private final HttpServer server;

    private final Map<String, > MethodHandlers

    public RequestHandlerResolver(HttpServer server) {
        this.server = server;
    }

    public void addController(Object controller) {
        Class<?> controllerClass = controller.getClass();
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        String basePath = requestMapping == null
                ? ""
                : requestMapping.path();

        for (Method method : controllerClass.getMethods()) {
            if(controllerClass.getAnnotations().length == 0) {
                break;
            }


            GetMapping getMapping = controllerClass.getAnnotation(GetMapping.class);


        }


             server.createContext();
    }
}
