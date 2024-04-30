/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.repositories.MessageRepository;

import java.util.List;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;

public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages(long idFrom, long idTo) {
        if (idFrom > idTo)
            throw new ResponseException(BAD_REQUEST);
        if (idTo - idFrom > 10)
            throw new ResponseException(BAD_REQUEST);
        return messageRepository.findMany(idFrom, idTo);
    }

    public void createMessage(Message message) {
        messageRepository.save(message);
    }

    public void removeMessage(long messageId) {
        messageRepository.remove(messageId);
    }
}
