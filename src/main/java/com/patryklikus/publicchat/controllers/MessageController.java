/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import com.patryklikus.publicchat.https.annotations.Authenticated;
import com.patryklikus.publicchat.https.annotations.DeleteMapping;
import com.patryklikus.publicchat.https.annotations.PostMapping;
import com.patryklikus.publicchat.https.annotations.RequestMapping;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import com.patryklikus.publicchat.services.MessageService;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.*;

@RequestMapping(path = "/api/messages")
public class MessageController {
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    public MessageController(ObjectMapper objectMapper, MessageService messageService) {
        this.objectMapper = objectMapper;
        this.messageService = messageService;
    }

    @Authenticated
    @PostMapping
    public Response createMessage(Request request) {
        Message message = objectMapper.toMessage(request.getAuthentication().userId(), request.getRequestBody());
        messageService.createMessage(message);
        return new Response(NO_CONTENT);
    }

    @Authenticated
    @DeleteMapping
    public Response deleteMessage(Request request) {
        if (!request.getAuthentication().isAdmin()) {
            return new Response(FORBIDDEN);
        }

        String messageId = request.getRequestURI()
                .getPath()
                .replace("/api/message/", "");

        long parsedId;
        try {
            parsedId = Long.parseLong(messageId);
        } catch (NumberFormatException e) {
            return new Response(BAD_REQUEST);
        }
        messageService.removeMessage(Long.parseLong(messageId));
        return new Response(NO_CONTENT);
    }
}
