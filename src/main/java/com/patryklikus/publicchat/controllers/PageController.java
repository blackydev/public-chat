/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.GetMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.services.ReaderService;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.OK;

public class PageController {
    private final ReaderService readerService;

    public PageController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public Response home(Request request) {
        return getHtmlResourceResponse("home");
    }

    @GetMapping(path = "/login")
    public Response login(Request request) {
        return getHtmlResourceResponse("login");

    }

    @GetMapping(path = "/register")
    public Response register(Request request) {
        return getHtmlResourceResponse("register");
    }

    @GetMapping(path = "/settings/me")
    public Response userUpdate(Request request) {
        return getHtmlResourceResponse("userUpdate");
    }

    private Response getHtmlResourceResponse(String resourceUri) {
        String content = readerService.readResource(resourceUri + ".html");
        return new Response(OK, content);
    }
}
