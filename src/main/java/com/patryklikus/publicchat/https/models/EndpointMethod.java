/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.models;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.INTERNAL_SERVER_ERROR;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return method.getAnnotation(annotationClass);
    }
}
