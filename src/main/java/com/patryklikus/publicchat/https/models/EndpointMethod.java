package com.patryklikus.publicchat.https.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.INTERNAL_SERVER_ERROR;

public record EndpointMethod<T>(T obj, Method method) {
    public Response apply(Request request) {
        try {
            return (Response) method.invoke(obj, request);
        } catch (InvocationTargetException | IllegalAccessException | RuntimeException e) {
            if (e.getCause().getCause() instanceof ResponseException re) {
                return new Response(re.getStatusCode(), re.getMessage());
            }
            return new Response(INTERNAL_SERVER_ERROR, "Unexpected server error");
        }
    }
}
