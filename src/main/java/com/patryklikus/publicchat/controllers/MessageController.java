/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;
import static com.patryklikus.publicchat.https.models.ResponseStatusCode.NO_CONTENT;
import static java.lang.Long.parseLong;

import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.dtos.GetMessagesRangeDto;
import com.patryklikus.publicchat.models.mappers.MessageMapper;
import com.patryklikus.publicchat.services.MessageService;
import java.util.List;

@RequestMapping(path = "/api/messages")
public class MessageController {
    private final MessageMapper messageMapper;
    private final MessageService messageService;

    public MessageController(MessageMapper messageMapper, MessageService messageService) {
        this.messageMapper = messageMapper;
        this.messageService = messageService;
    }

    @Authenticated
    @GetMapping(path = "/last/id")
    public Response getLastMessageId(Request request) {
        Message message = messageService.getLastMessage();
        if (message == null) {
            return new Response("-1");
        }
        return new Response(message.getId().toString());
    }

    @Authenticated
    @GetMapping
    public Response getMessages(Request request) {
        GetMessagesRangeDto messageRange = messageMapper.toMessageRangeDto(request.getRequestURI().getQuery());
        List<Message> messages = messageService.getMessages(messageRange);
        return new Response(messageMapper.toJson(messages));
    }

    @Authenticated
    @PostMapping
    public Response createMessage(Request request) {
        Message message = messageMapper.toMessage(request.getAuthentication().userId(), request.getRequestBody());
        messageService.createMessage(message);
        return new Response(NO_CONTENT);
    }

    @Authenticated(admin = true)
    @DeleteMapping
    public Response deleteMessage(Request request) {
        String messageId = request.getRequestURI().getPath().replace("/api/messages/", "");
        try {
            messageService.removeMessage(parseLong(messageId));
        } catch (NumberFormatException e) {
            return new Response(BAD_REQUEST);
        }
        return new Response(NO_CONTENT);
    }
}
