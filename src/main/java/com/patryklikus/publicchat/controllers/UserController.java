/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;


import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.mappers.AuthMapper;
import com.patryklikus.publicchat.models.mappers.JsonMapper;
import com.patryklikus.publicchat.models.mappers.UserMapper;
import com.patryklikus.publicchat.services.UserService;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.NO_CONTENT;

@RequestMapping(path = "/api/users")
public class UserController {
    private final AuthMapper authMapper;
    private final JsonMapper jsonMapper;
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(AuthMapper authMapper, JsonMapper jsonMapper, UserMapper userMapper, UserService userService) {
        this.authMapper = authMapper;
        this.jsonMapper = jsonMapper;
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
    @PostMapping(path = "/permissions/admin")
    public Response addUserAdminPerms(Request request) {
        String username = jsonMapper.jsonStringToString(request.getRequestBody());
        userService.setAdminPerms(username, true);
        return new Response(NO_CONTENT);
    }

    @Authenticated(admin = true)
    @DeleteMapping(path = "/permissions/admin")
    public Response removeUserAdminPerms(Request request) {
        String username = jsonMapper.jsonStringToString(request.getRequestBody());
        userService.setAdminPerms(username, false);
        return new Response(NO_CONTENT);
    }
}
