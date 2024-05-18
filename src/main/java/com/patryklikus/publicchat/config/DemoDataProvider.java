/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.https.models.ResponseException;
import com.patryklikus.publicchat.models.Message;
import com.patryklikus.publicchat.models.MessageBuilder;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.models.UserBuilder;
import com.patryklikus.publicchat.services.MessageService;
import com.patryklikus.publicchat.services.ReaderService;
import com.patryklikus.publicchat.services.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class DemoDataProvider {
    private static final Logger LOG = Logger.getLogger(DemoDataProvider.class.getName());
    private static final String DEMO_PASSWORD = "Pass123";
    private final UserService userService;
    private final MessageService messageService;
    private final ReaderService readerService;

    public DemoDataProvider(UserService userService, MessageService messageService, ReaderService readerService) {
        this.userService = userService;
        this.messageService = messageService;
        this.readerService = readerService;
    }

    public void init() {
        try {
            initUnsafe();
        } catch (ResponseException e) {
            LOG.info("Data is already initialized");
        } catch (RuntimeException e) {
            LOG.warning("Some problems with initializing demo data: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void initUnsafe() {
        LOG.info("Start initializing demo data");
        initRootUser();
        List<User> users = initUsers();
        int index = 0;
        for (String content : readerService.readResource("./demo/messages.txt").split("\n")) {
            System.out.println(content);
            User author = users.get(index++ % users.size());
            Message message = MessageBuilder.aMessage()
                    .withContent(content)
                    .withAuthor(author)
                    .build();
            messageService.createMessage(message);
        }
        LOG.info("End initializing demo data");
    }

    private void initRootUser() {
        User root = UserBuilder.anUser()
                .withUsername("root")
                .withPassword(DEMO_PASSWORD)
                .withIsAdmin(true).build();
        userService.createUser(root);
    }

    private List<User> initUsers() {
        List<User> users = new ArrayList<>();
        User user = UserBuilder.anUser()
                .withUsername("John")
                .withPassword(DEMO_PASSWORD)
                .withIsAdmin(false).build();
        users.add(user);

        user = UserBuilder.anUser()
                .withUsername("Anna")
                .withPassword(DEMO_PASSWORD)
                .withIsAdmin(false).build();
        users.add(user);

        user = UserBuilder.anUser()
                .withUsername("Black Monster")
                .withPassword(DEMO_PASSWORD)
                .withIsAdmin(false).build();
        users.add(user);

        user = UserBuilder.anUser()
                .withUsername("Penny")
                .withPassword(DEMO_PASSWORD)
                .withIsAdmin(false).build();
        users.add(user);

        users.forEach(userService::createUser);
        return users;
    }
}
