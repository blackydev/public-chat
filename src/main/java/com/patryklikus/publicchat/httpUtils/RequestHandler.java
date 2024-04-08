package com.patryklikus.publicchat.httpUtils;

import com.patryklikus.publicchat.httpUtils.Request.Request;
import com.patryklikus.publicchat.httpUtils.Response.Response;
import com.patryklikus.publicchat.httpUtils.Response.ResponseStatusCode;
import com.patryklikus.publicchat.httpUtils.Response.StringResponseSender;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.function.Function;

public class RequestHandler implements HttpHandler {
    private final StringResponseSender responseSender;
    private final Function<Request, Response> getHandler;
    private final Function<Request, Response> postHandler;
    private final Function<Request, Response> putMethod;
    private final Function<Request, Response> deleteMethod;

    public RequestHandler(StringResponseSender responseSender, Function<Request, Response> getHandler, Function<Request, Response> postHandler, Function<Request, Response> putMethod, Function<Request, Response> deleteMethod) {
        this.responseSender = responseSender;
        this.getHandler = getHandler;
        this.postHandler = postHandler;
        this.putMethod = putMethod;
        this.deleteMethod = deleteMethod;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        Request request = Request.create(exchange);
        Function<Request, Response> methodHandler;
        if ("get".equals(method)) {
            methodHandler = getHandler;
        } else if ("post".equals(method)) {
            methodHandler = postHandler;
        } else if ("put".equals(method)) {
            methodHandler = putMethod;
        } else if ("delete".equals(method)) {
            methodHandler = deleteMethod;
        } else {
            var response = new Response(ResponseStatusCode.BAD_REQUEST, "This website is unavailable");
            responseSender.send(exchange, response); // todo add website sender
            return;
        }

        methodHandler.apply(request);
    }
}
