/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.PostMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.mappers.AuthMapper;
import com.patryklikus.publicchat.models.mappers.UserMapper;
import com.patryklikus.publicchat.services.AuthService;

@RequestMapping(path = "/api/auth")
public class AuthController {
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final AuthService authService;

    public AuthController(AuthMapper authMapper, UserMapper userMapper, AuthService authService) {
        this.authMapper = authMapper;
        this.userMapper = userMapper;
        this.authService = authService;
    }

    @PostMapping
    public Response authenticate(Request request) {
        User user = userMapper.toUser(request.getRequestBody());
        Authentication authentication = authService.authenticate(user);
        return new Response(authMapper.toJson(authentication));
    }
}
