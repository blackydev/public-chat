/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.request.Request;
import com.patryklikus.publicchat.https.request.methodMappings.GetMapping;
import com.patryklikus.publicchat.https.request.methodMappings.RequestMapping;
import com.patryklikus.publicchat.https.response.Response;
import com.patryklikus.publicchat.https.response.ResponseStatusCode;
import com.patryklikus.publicchat.services.PublicService;

@RequestMapping(path = "/public")
public class PublicController {
    private final PublicService publicService;

    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

    @GetMapping
    public Response getPublicResource(Request request) {
        String content = publicService.getPublicResource("." + request.getRequestURI().toString());
        if (content == null) {
            return new Response(ResponseStatusCode.NOT_FOUND, "Resource not found");
        } else {
            return new Response(ResponseStatusCode.OK, content);
        }
    }
}
