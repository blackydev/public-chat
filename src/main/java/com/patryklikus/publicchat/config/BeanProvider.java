/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.controllers.*;
import com.patryklikus.publicchat.models.mappers.JsonMapper;
import com.patryklikus.publicchat.models.mappers.MessageMapper;
import com.patryklikus.publicchat.models.mappers.QueryMapper;
import com.patryklikus.publicchat.models.mappers.UserMapper;
import com.patryklikus.publicchat.repositories.MessageRepository;
import com.patryklikus.publicchat.repositories.UserRepository;
import com.patryklikus.publicchat.services.*;

public class BeanProvider {
    static final PostgresClient POSTGRESQL_CLIENT = new PostgresClient("jdbc:postgresql://localhost:5432/db", "wdpai", "password");

    /**
     * MAPPERS
     */
    private static final JsonMapper JSON_MAPPER = new JsonMapper();
    private static final QueryMapper QUERY_MAPPER = new QueryMapper();
    private static final UserMapper USER_MAPPER = new UserMapper(JSON_MAPPER);
    private static final MessageMapper MESSAGE_MAPPER = new MessageMapper(JSON_MAPPER, QUERY_MAPPER);

    /**
     * REPOSITORIES
     */
    static final UserRepository USER_REPOSITORY = new UserRepository(POSTGRESQL_CLIENT);
    static final MessageRepository MESSAGE_REPOSITORY = new MessageRepository(POSTGRESQL_CLIENT);

    /**
     * SERVICES
     */
    private static final ReaderService READER_SERVICE = new ReaderService();
    private static final HashingService HASHING_SERVICE = new HashingService();
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY, HASHING_SERVICE);
    private static final MessageService MESSAGE_SERVICE = new MessageService(MESSAGE_REPOSITORY);
    public static final AuthService AUTH_SERVICE = new AuthService(HASHING_SERVICE, USER_REPOSITORY);

    /**
     * CONTROLLERS
     */
    static final PublicController PUBLIC_CONTROLLER = new PublicController(READER_SERVICE);
    static final PageController PAGE_CONTROLLER = new PageController(READER_SERVICE);
    static final AuthController AUTH_CONTROLLER = new AuthController(AUTH_SERVICE);
    static final UserController USER_CONTROLLER = new UserController(USER_MAPPER, USER_SERVICE);
    static final MessageController MESSAGE_CONTROLLER = new MessageController(MESSAGE_MAPPER, MESSAGE_SERVICE);
}
