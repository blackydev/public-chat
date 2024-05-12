/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.NO_CONTENT;

import com.patryklikus.publicchat.https.annotations.Authenticated;
import com.patryklikus.publicchat.https.annotations.PostMapping;
import com.patryklikus.publicchat.https.annotations.PutMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.mappers.AuthMapper;
import com.patryklikus.publicchat.models.mappers.UserMapper;
import com.patryklikus.publicchat.services.UserService;

@RequestMapping(path = "/api/users")
public class UserController {
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(AuthMapper authMapper, UserMapper userMapper, UserService userService) {
        this.authMapper = authMapper;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping
    public Response createUser(Request request) {
        User user = userMapper.toUser(request.getRequestBody());
        userService.createUser(user);
        Authentication authentication = new Authentication(user);
        return new Response(authMapper.toJson(authentication));
    }

    @Authenticated
    @PutMapping(path = "/me")
    public Response updateUser(Request request) {
        User user = userMapper.toUser(request.getRequestBody());
        userService.updateUser(request.getAuthentication(), user);
        return new Response(NO_CONTENT);
    }
}
