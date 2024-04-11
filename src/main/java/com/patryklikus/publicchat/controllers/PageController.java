/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.request.Request;
import com.patryklikus.publicchat.https.request.methodMappings.GetMapping;
import com.patryklikus.publicchat.https.response.Response;
import com.patryklikus.publicchat.https.response.ResponseStatusCode;
import com.patryklikus.publicchat.services.ReaderService;

public class PageController {
    private final ReaderService readerService;

    public PageController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public Response home(Request request) {
        String content = readerService.readResource("home.html");
        return new Response(ResponseStatusCode.OK, content);
    }

    @GetMapping(path = "/login")
    public Response login(Request request) {
        String content = readerService.readResource("login.html");
        return new Response(ResponseStatusCode.OK, content);
    }

    @GetMapping(path = "/register")
    public Response register(Request request) {
        String content = readerService.readResource("register.html");
        return new Response(ResponseStatusCode.OK, content);
    }
}
