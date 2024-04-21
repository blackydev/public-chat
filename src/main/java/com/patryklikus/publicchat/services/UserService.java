/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;

import com.patryklikus.publicchat.exceptions.ResponseException;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (user == null) {
            throw new ResponseException(BAD_REQUEST, "Invalid request body");
        }
        return userRepository.save(user);
    }
}
