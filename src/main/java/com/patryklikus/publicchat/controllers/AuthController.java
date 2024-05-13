/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.UNAUTHORIZED;

import com.patryklikus.publicchat.https.annotations.PostMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.mappers.AuthMapper;
import com.patryklikus.publicchat.services.AuthService;

@RequestMapping(path = "/api/auth")
public class AuthController {
    private final AuthMapper authMapper;
    private final AuthService authService;

    public AuthController(AuthMapper authMapper, AuthService authService) {
        this.authMapper = authMapper;
        this.authService = authService;
    }

    @PostMapping
    public Response authenticate(Request request) {
        return authService.authenticate(request.getRequestHeaders()) == null
                ? new Response(UNAUTHORIZED, "Invalid username or password")
                : new Response(authMapper.toJson(request.getAuthentication()));
    }
}
