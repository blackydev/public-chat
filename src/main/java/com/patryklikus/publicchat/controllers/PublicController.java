/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.GetMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.https.models.ResponseStatusCode;
import com.patryklikus.publicchat.services.ReaderService;

@RequestMapping(path = "/public")
public class PublicController {
    private final ReaderService readerService;

    public PublicController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public Response getPublicResource(Request request) {
        String content = readerService.readResource("." + request.getRequestURI().toString());
        if (content == null) {
            return new Response(ResponseStatusCode.NOT_FOUND, "Resource not found");
        } else {
            return new Response(ResponseStatusCode.OK, content);
        }
    }
}
