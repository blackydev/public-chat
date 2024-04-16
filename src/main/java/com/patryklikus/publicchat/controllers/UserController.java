/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.request.Request;
import com.patryklikus.publicchat.https.request.methodMappings.PostMapping;
import com.patryklikus.publicchat.https.request.methodMappings.RequestMapping;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;

@RequestMapping(path = "/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User createUser(Request request) {
    }
}
