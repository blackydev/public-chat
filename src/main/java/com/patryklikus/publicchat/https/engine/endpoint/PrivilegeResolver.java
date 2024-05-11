/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine.endpoint;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.FORBIDDEN;
import static com.patryklikus.publicchat.https.models.ResponseStatusCode.UNAUTHORIZED;

import com.patryklikus.publicchat.https.annotations.Authenticated;
import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.EndpointMethod;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.ResponseException;

public class PrivilegeResolver {
    public void test(EndpointMethod endpointMethod, Request request) {
        Authentication authentication = request.getAuthentication();
        if (endpointMethod.isAnnotationPresent(Authenticated.class)) {
            if (authentication == null)
                throw new ResponseException(UNAUTHORIZED);
            if (endpointMethod.getAnnotation(Authenticated.class).admin() && !authentication.isAdmin())
                throw new ResponseException(FORBIDDEN);
        }
    }
}
