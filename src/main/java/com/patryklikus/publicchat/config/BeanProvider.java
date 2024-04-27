/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.controllers.AuthController;
import com.patryklikus.publicchat.controllers.PageController;
import com.patryklikus.publicchat.controllers.PublicController;
import com.patryklikus.publicchat.controllers.UserController;
import com.patryklikus.publicchat.models.mappers.JsonMapper;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import com.patryklikus.publicchat.repositories.MessageRepository;
import com.patryklikus.publicchat.repositories.UserRepository;
import com.patryklikus.publicchat.services.AuthService;
import com.patryklikus.publicchat.services.HashingService;
import com.patryklikus.publicchat.services.ReaderService;
import com.patryklikus.publicchat.services.UserService;

public class BeanProvider {
    private static final PostgresClient POSTGRESQL_CLIENT = new PostgresClient("jdbc:postgresql://localhost:5432/db", "wdpai", "password");
    // MAPPERS
    private static final JsonMapper JSON_MAPPER = new JsonMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(JSON_MAPPER);
    // REPOSITORIES
    private static final UserRepository USER_REPOSITORY = new UserRepository(POSTGRESQL_CLIENT);
    private static final MessageRepository MESSAGE_REPOSITORY = new MessageRepository(POSTGRESQL_CLIENT);
    // SERVICES
    private static final ReaderService READER_SERVICE = new ReaderService();
    private static final HashingService HASHING_SERVICE = new HashingService();
    private static final AuthService AUTH_SERVICE = new AuthService(HASHING_SERVICE, USER_REPOSITORY);
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY, HASHING_SERVICE);
    // CONTROLLERS
    private static final PublicController PUBLIC_CONTROLLER = new PublicController(READER_SERVICE);
    private static final PageController PAGE_CONTROLLER = new PageController(READER_SERVICE);
    private static final AuthController AUTH_CONTROLLER = new AuthController(AUTH_SERVICE);
    private static final UserController USER_CONTROLLER = new UserController(OBJECT_MAPPER, USER_SERVICE);

    public static PostgresClient getPostgresqlClient() {
        return POSTGRESQL_CLIENT;
    }

    public static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    public static MessageRepository getMessageRepository() {
        return MESSAGE_REPOSITORY;
    }


    public static AuthService getAuthService() {
        return AUTH_SERVICE;
    }

    public static PublicController getPublicController() {
        return PUBLIC_CONTROLLER;
    }

    public static PageController getPageController() {
        return PAGE_CONTROLLER;
    }

    public static UserController getUserController() {
        return USER_CONTROLLER;
    }

    public static AuthController getAuthController() {
        return AUTH_CONTROLLER;
    }
}
