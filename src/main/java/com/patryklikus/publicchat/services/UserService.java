/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.patryklikus.publicchat.exceptions.ResponseException;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;

public class UserService {
    private final UserRepository userRepository;
    private final HashingService hashingService;

    public UserService(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    public void createUser(User user) {
        if (user == null) {
            throw new ResponseException(BAD_REQUEST, "Invalid request body");
        }

        String password = hashingService.hash(user.getPassword());
        user.setPassword(password);

        userRepository.save(user);
    }
}
