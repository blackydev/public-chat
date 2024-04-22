/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import com.patryklikus.publicchat.services.UserService;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.NO_CONTENT;

@RequestMapping(path = "/api/posts")
public class PostController {
    private final ObjectMapper objectMapper;
    private final PostService postService;

    public PostController(ObjectMapper objectMapper, PostService postService) {
        this.objectMapper = objectMapper;
        this.postService = postService;
    }

    @Authenticated
    @PostMapping
    public Response createPost(Request request) {
        User user = objectMapper.toUser(request.getRequestBody());
        postService.createPost(user);
        return new Response(NO_CONTENT);
    }

    @Authenticated
    @DeleteMapping
    public Response deletePost(Request request) {
        User user = objectMapper.toUser(request.getRequestBody());
        postService.deletePost(request.getAuthentication(), user);
        return new Response(NO_CONTENT);
    }
}
