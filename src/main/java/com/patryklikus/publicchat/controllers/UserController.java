/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.request.Request;
import com.patryklikus.publicchat.https.request.methodMappings.PostMapping;
import com.patryklikus.publicchat.https.request.methodMappings.RequestMapping;
import com.patryklikus.publicchat.https.response.Response;
import com.patryklikus.publicchat.https.response.ResponseStatusCode;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import com.patryklikus.publicchat.services.UserService;

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
        return new Response(ResponseStatusCode.NO_CONTENT, "");
    }
}
