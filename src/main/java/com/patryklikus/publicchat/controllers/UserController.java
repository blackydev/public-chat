/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.Authenticated;
import com.patryklikus.publicchat.https.annotations.PostMapping;
import com.patryklikus.publicchat.https.annotations.PutMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.mappers.UserMapper;
import com.patryklikus.publicchat.services.UserService;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.NO_CONTENT;

@RequestMapping(path = "/api/users")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping
    public Response createUser(Request request) {
        User user = userMapper.toUser(request.getRequestBody());
        userService.createUser(user);
        return new Response(NO_CONTENT);
    }

    @Authenticated
    @PutMapping(path = "/me")
    public Response updateUser(Request request) {
        User user = userMapper.toUser(request.getRequestBody());
        userService.updateUser(request.getAuthentication(), user);
        return new Response(NO_CONTENT);
    }
}
