/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine.endpoint.method;

import com.patryklikus.publicchat.https.models.EndpointMethod;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.RequestMethod;
import java.util.function.Function;

public class EndpointMethodChain implements Function<Request, EndpointMethod> {
    private final RequestMethod method;
    private final EndpointMethod endpointMethod;

    public EndpointMethodChain(RequestMethod method, EndpointMethod endpointMethod) {
        this.method = method;
        this.endpointMethod = endpointMethod;
    }

    public EndpointMethod apply(Request exchange) {
        if (method.name().equals(exchange.getRequestMethod())) {
            return endpointMethod;
        }
        return null;
    }
}
