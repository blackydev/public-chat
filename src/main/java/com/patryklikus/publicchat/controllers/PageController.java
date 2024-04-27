/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.OK;

import com.patryklikus.publicchat.https.annotations.GetMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.services.ReaderService;

public class PageController {
    private final ReaderService readerService;

    public PageController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public Response home(Request request) {
        String content = readerService.readResource("home.html");
        return new Response(OK, content);
    }

    @GetMapping(path = "/login")
    public Response login(Request request) {
        String content = readerService.readResource("login.html");
        return new Response(OK, content);
    }

    @GetMapping(path = "/register")
    public Response register(Request request) {
        String content = readerService.readResource("register.html");
        return new Response(OK, content);
    }
}
