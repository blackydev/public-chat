/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.controllers.PageController;
import com.patryklikus.publicchat.controllers.PublicController;
import com.patryklikus.publicchat.controllers.UserController;
import com.patryklikus.publicchat.models.mappers.ObjectMapper;
import com.patryklikus.publicchat.repositories.UserRepository;
import com.patryklikus.publicchat.services.AuthService;
import com.patryklikus.publicchat.services.HashingService;
import com.patryklikus.publicchat.services.ReaderService;
import com.patryklikus.publicchat.services.UserService;

public class BeanProvider {
    private static final ReaderService READER_SERVICE = new ReaderService();
    private static final PublicController PUBLIC_CONTROLLER = new PublicController(READER_SERVICE);
    private static final PageController PAGE_CONTROLLER = new PageController(READER_SERVICE);
    private static final PostgresClient POSTGRESQL_CLIENT = new PostgresClient("jdbc:postgresql://localhost:5432/db", "wdpai", "password");

    private static final HashingService HASHING_SERVICE = new HashingService();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserRepository USER_REPOSITORY = new UserRepository(POSTGRESQL_CLIENT);
    private static final AuthService AUTH_SERVICE = new AuthService(HASHING_SERVICE, USER_REPOSITORY);
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY, HASHING_SERVICE);
    private static final UserController USER_CONTROLLER = new UserController(OBJECT_MAPPER, USER_SERVICE);

    public static UserController getUserController() {
        return USER_CONTROLLER;
    }

    public static AuthService getAuthService() {
        return AUTH_SERVICE;
    }

    public static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    public static PublicController getPublicController() {
        return PUBLIC_CONTROLLER;
    }

    public static PageController getPageController() {
        return PAGE_CONTROLLER;
    }

    public static PostgresClient getPostgresqlClient() {
        return POSTGRESQL_CLIENT;
    }
}
