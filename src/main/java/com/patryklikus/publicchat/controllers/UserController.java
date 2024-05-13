/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;


import static com.patryklikus.publicchat.https.models.ResponseStatusCode.*;

import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.https.models.ResponseException;
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
        Authentication authentication = new Authentication(user);
        return new Response(authMapper.toJson(authentication));
    }

    @Authenticated(admin = true)
    @PostMapping
    public Response addUserAdminPerms(Request request) {
        String uri = request.getRequestURI().toString();
        if (!uri.endsWith("/permissions/admin")) {
            return new Response(NOT_FOUND);
        }
        long userId = extractUserId(uri);
        userService.setAdminPerms(userId, true);
        return new Response(NO_CONTENT);
    }

    @Authenticated(admin = true)
    @DeleteMapping
    public Response removeUserAdminPerms(Request request) {
        String uri = request.getRequestURI().toString();
        if (!uri.endsWith("/permissions/admin")) {
            return new Response(NOT_FOUND);
        }
        long userId = extractUserId(uri);
        userService.setAdminPerms(userId, false);
        return new Response(NO_CONTENT);
    }

    private long extractUserId(String uri) {
        try {
            uri = uri.replace("/api/users/", "").replace("/permissions/admin", "");
            return Long.parseLong(uri);
        } catch (RuntimeException e) {
            throw new ResponseException(BAD_REQUEST);
        }
    }
}
