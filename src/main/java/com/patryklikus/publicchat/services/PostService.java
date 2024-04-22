/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.patryklikus.publicchat.https.models.Authentication;
import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.models.Post;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;

import java.util.List;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;
import static com.patryklikus.publicchat.https.models.ResponseStatusCode.CONFLICT;

public class PostService {
    private final UserRepository userRepository;
    private final HashingService hashingService;

    public PostService(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    public List<Post> getPosts() {
        return List.of();
    }

    public void createPost(Post post) {
    }

    public void removePost(Authentication authentication, Post post) {
    }
}
