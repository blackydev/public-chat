/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.*;

import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private final HashingService hashingService;

    public UserService(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    public void createUser(User user) {
        checkConflict(user);
        saveUser(user);
    }

    public void updateUser(Authentication authentication, User user) {
        checkConflict(user);
        user.setId(authentication.userId());
        saveUser(user);
    }

    public void setAdminPerms(String username, boolean adminPerms) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseException(NOT_FOUND);
        }
        user.setAdmin(adminPerms);
        userRepository.save(user);
    }

    private void checkConflict(User user) {
        if (user == null) {
            throw new ResponseException(BAD_REQUEST, "Invalid request body");
        }
        User found = userRepository.findByUsername(user.getUsername());
        if (found != null) {
            throw new ResponseException(CONFLICT, "User with provided username already exists");
        }
    }

    private void saveUser(User user) {
        String password = hashingService.hash(user.getPassword());
        user.setPassword(password);
        userRepository.save(user);
    }
}
