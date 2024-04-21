/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.PostMapping;
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

    /*@GetMapping
    public Response getUser(Request request) {
        String[] pathParts = request.getRequestURI()
                .getPath()
                .replace("/api/users/", "")
                .split("/");
        if(pathParts.length != 1) {
            throw new ResponseException(NOT_FOUND);
        }
        String endpoint = pathParts[0];
        return new Response(endpoint);
    }*/

    @PostMapping
    public Response createUser(Request request) {
        User user = objectMapper.toUser(request.getRequestBody());
        userService.createUser(user);
        return new Response(NO_CONTENT, "");
    }
}
