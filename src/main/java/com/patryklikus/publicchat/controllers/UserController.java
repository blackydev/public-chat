/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.Authenticated;
import com.patryklikus.publicchat.https.annotations.PostMapping;
import com.patryklikus.publicchat.https.annotations.PutMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import com.patryklikus.publicchat.services.UserService;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.NO_CONTENT;

@RequestMapping(path = "/api/users")
public class UserController {
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public UserController(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @PostMapping
    public Response createUser(Request request) {
        User user = objectMapper.toUser(request.getRequestBody());
        userService.createUser(user);
        return new Response(NO_CONTENT);
    }

    @Authenticated
    @PutMapping(path = "/me")
    public Response updateUser(Request request) {
        User user = objectMapper.toUser(request.getRequestBody());
        userService.updateUser(request.getAuthentication(), user);
        return new Response(NO_CONTENT);
    }
}
