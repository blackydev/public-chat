/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.patryklikus.publicchat.repositories.UserRepository;
import com.sun.net.httpserver.Headers;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authorize(Headers headers) {
        return true; // todo
    }
}
