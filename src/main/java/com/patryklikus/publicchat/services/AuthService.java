/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;
import com.sun.net.httpserver.Headers;
import java.util.Base64;
import java.util.List;

public class AuthService {
    private static final String AUTHORIZATION_PREFIX = "Basic ";
    private final HashingService hashingService;
    private final UserRepository userRepository;

    public AuthService(HashingService hashingService, UserRepository userRepository) {
        this.hashingService = hashingService;
        this.userRepository = userRepository;
    }

    public Authentication authenticate(Headers headers) {
        List<String> authorization = headers.get("Authorization");
        if (authorization == null || authorization.size() != 1) {
            return null;
        }
        String authHeader = authorization.getFirst();
        if (!authHeader.startsWith(AUTHORIZATION_PREFIX)) {
            return null;
        }
        String[] userPass = decodeCredentials(authHeader);
        User user = userRepository.findByUsername(userPass[0]);
        if (user != null && hashingService.compare(userPass[1], user.getPassword())) {
            return new Authentication(user);
        }
        return null;
    }

    private String[] decodeCredentials(String authHeader) {
        String credentials = authHeader.substring(AUTHORIZATION_PREFIX.length());
        byte[] decodedBytes = Base64.getDecoder().decode(credentials); // todo will it work?
        return new String(decodedBytes, UTF_8).split(":");
    }
}
