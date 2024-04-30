/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.models.GetMessageRangeDto;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.repositories.MessageRepository;

import java.util.List;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;

public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages(GetMessageRangeDto messageRange) {
        if (messageRange.minId() > messageRange.maxId())
            throw new ResponseException(BAD_REQUEST);
        if (messageRange.maxId() - messageRange.minId() > 10)
            throw new ResponseException(BAD_REQUEST);
        return messageRepository.findMany(messageRange.minId(), messageRange.maxId());
    }

    public void createMessage(Message message) {
        messageRepository.save(message);
    }

    public void removeMessage(long messageId) {
        messageRepository.remove(messageId);
    }
}
