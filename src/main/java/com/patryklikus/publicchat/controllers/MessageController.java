/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.controllers;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.*;

import com.patryklikus.publicchat.https.annotations.*;
import com.patryklikus.publicchat.https.models.Request;
import com.patryklikus.publicchat.https.models.Response;
import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import com.patryklikus.publicchat.services.MessageService;
import java.util.List;
import java.util.Map;

@RequestMapping(path = "/api/messages")
public class MessageController {
    private final ObjectMapper objectMapper;
    private final MessageService messageService;

    public MessageController(ObjectMapper objectMapper, MessageService messageService) {
        this.objectMapper = objectMapper;
        this.messageService = messageService;
    }

    @Authenticated
    @GetMapping
    public Response getMessages(Request request) {
        Map<String, String> queries = objectMapper.queryToMap(request.getRequestURI().getQuery());
        long idFrom = mapToLong(queries.get("idFrom"));
        long idTo = mapToLong(queries.get("idTo"));
        List<Message> messages = messageService.getMessages(idFrom, idTo);
        String json = objectMapper.toJson(messages);
        return new Response(json);
    }

    @Authenticated
    @PostMapping
    public Response createMessage(Request request) {
        Message message = objectMapper.toMessage(request.getAuthentication().userId(), request.getRequestBody());
        messageService.createMessage(message);
        return new Response(NO_CONTENT);
    }

    @Authenticated(admin = true)
    @DeleteMapping
    public Response deleteMessage(Request request) {
        String messageId = request.getRequestURI().getPath().replace("/api/message/", "");
        messageService.removeMessage(mapToLong(messageId));
        return new Response(NO_CONTENT);
    }

    private long mapToLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new ResponseException(BAD_REQUEST);
        }
    }
}