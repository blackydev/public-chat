/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;
import java.util.Base64;
import java.util.List;

public class AuthService {
    private final HashingService hashingService;
    private final UserRepository userRepository;

    public AuthService(HashingService hashingService, UserRepository userRepository) {
        this.hashingService = hashingService;
        this.userRepository = userRepository;
    }

    public Authentication authenticate(HttpExchange exchange) {
        List<String> authorization = exchange.getRequestHeaders().get("Authorization");
        if (authorization.size() != 1) {
            return null;
        }
        String authHeader = authorization.getFirst();
        if (!authHeader.startsWith("Basic ")) {
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
        String credentials = authHeader.substring("Basic ".length());
        byte[] decodedBytes = Base64.getDecoder().decode(credentials); // todo
        String decoded = new String(decodedBytes, UTF_8);
        return decoded.split(":");
    }
}
