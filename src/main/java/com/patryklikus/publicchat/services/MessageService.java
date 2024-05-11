/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import static com.patryklikus.publicchat.https.models.ResponseStatusCode.BAD_REQUEST;

import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.dtos.GetMessagesRangeDto;
import com.patryklikus.publicchat.repositories.MessageRepository;
import java.util.List;

public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message getLastMessage() {
        return messageRepository.findLast();
    }

    public List<Message> getMessages(GetMessagesRangeDto messageRange) {
        if (messageRange.minId() > messageRange.maxId())
            throw new ResponseException(BAD_REQUEST);
        if (messageRange.maxId() - messageRange.minId() > 10)
            throw new ResponseException(BAD_REQUEST);
        System.out.println("asd");
        return messageRepository.findMany(messageRange.minId(), messageRange.maxId());
    }

    public void createMessage(Message message) {
        messageRepository.save(message);
    }

    public void removeMessage(long messageId) {
        messageRepository.remove(messageId);
    }
}
