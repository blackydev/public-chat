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
        String resourceUrl = request.getRequestURI().toString()
                .replaceFirst("/", "");
        String content = readerService.readResource(resourceUrl);
        return content == null
                ? new Response(ResponseStatusCode.NOT_FOUND, "Resource not found")
                : new Response(ResponseStatusCode.OK, content);
    }
}
