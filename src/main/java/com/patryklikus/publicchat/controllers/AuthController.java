/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.PostMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.services.AuthService;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.NO_CONTENT;
import static com.patryklikus.publicchat.https.models.ResponseStatusCode.UNAUTHORIZED;

@RequestMapping(path = "/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public Response authenticate(Request request) {
        return authService.authenticate(request.getRequestHeaders()) == null
                ? new Response(UNAUTHORIZED, "Invalid username or password")
                : new Response(NO_CONTENT);
    }
}
