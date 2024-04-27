/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.repositories.MessageRepository;

import java.util.List;

import static com.patryklikus.publicchat.models.PostBuilder.aMessage;

public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages() {
        return List.of();
    }

    public void createMessage(Message message) {
        messageRepository.save(message);
    }

    public void removeMessage(long messageId) {
        messageRepository.remove(aMessage().withId(messageId).build());
    }
}
