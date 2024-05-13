/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.MessageBuilder;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.UserBuilder;
import com.patryklikus.publicchat.services.MessageService;
import com.patryklikus.publicchat.services.ReaderService;
import com.patryklikus.publicchat.services.UserService;
import java.util.ArrayList;
import java.util.List;

public class DemoDataProvider {
    private final UserService userService;
    private final MessageService messageService;
    private final ReaderService readerService;

    public DemoDataProvider(UserService userService, MessageService messageService, ReaderService readerService) {
        this.userService = userService;
        this.messageService = messageService;
        this.readerService = readerService;
    }

    public void init() {
        initRootUser();
        List<User> users = initUsers();
        int index = 0;
        for (String content : readerService.readResource("/demo/messages.txt").split("\n")) {
            User author = users.get(index++ % users.size());
            Message message = MessageBuilder.aMessage()
                    .withContent(content)
                    .withAuthor(author)
                    .build();
            messageService.createMessage(message);
        }
    }

    private void initRootUser() {
        User root = UserBuilder.anUser()
                .withUsername("root")
                .withPassword("LongPass123")
                .withIsAdmin(true).build();
        userService.createUser(root);
    }

    private List<User> initUsers() {
        List<User> users = new ArrayList<>();
        User user = UserBuilder.anUser()
                .withUsername("John")
                .withPassword("Pass123").build();
        users.add(user);

        user = UserBuilder.anUser()
                .withUsername("Anna")
                .withPassword("Pass123").build();
        users.add(user);

        user = UserBuilder.anUser()
                .withUsername("Black Monster")
                .withPassword("Pass123").build();
        users.add(user);

        user = UserBuilder.anUser()
                .withUsername("Penny")
                .withPassword("Pass123").build();
        users.add(user);

        users.forEach(userService::createUser);
        return users;
    }
}
