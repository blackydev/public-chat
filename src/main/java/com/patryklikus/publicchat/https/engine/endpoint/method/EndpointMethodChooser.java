/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.engine.endpoint.method;

import com.patryklikus.publicchat.https.models.EndpointMethod;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.RequestMethod;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class EndpointMethodChooser implements Function<Request, EndpointMethod> {
    private final List<EndpointMethodChain> endpointMethodChains = new LinkedList<>();

    @Override
    public EndpointMethod apply(Request request) {
        for (EndpointMethodChain method : endpointMethodChains) {
            EndpointMethod endpointMethod = method.apply(request);
            if (endpointMethod != null) {
                return endpointMethod;
            }
        }
        return null;
    }

    public void addMethod(RequestMethod method, EndpointMethod endpointMethod) {
        EndpointMethodChain endpointMethodChain = new EndpointMethodChain(method, endpointMethod);
        endpointMethodChains.add(endpointMethodChain);
    }
}
