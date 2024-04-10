/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.engine.https.request.Request;
import com.patryklikus.publicchat.engine.https.request.methodMappings.GetMapping;
import com.patryklikus.publicchat.engine.https.response.Response;
import com.patryklikus.publicchat.engine.https.response.ResponseStatusCode;
import com.patryklikus.publicchat.services.ReaderService;

public class PageController {
    private final ReaderService readerService;

    public PageController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping(path = "/")
    public Response home(Request request) {
        String content = readerService.readResource("home.html");
        return new Response(ResponseStatusCode.OK, content);
    }
}
