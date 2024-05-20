/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.ShutdownHook;
import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.controllers.*;
import com.patryklikus.publicchat.models.mappers.*;
import com.patryklikus.publicchat.repositories.MessageRepository;
import com.patryklikus.publicchat.repositories.UserRepository;
import com.patryklikus.publicchat.services.*;

public class BeanProvider {
    static final PostgresClient POSTGRESQL_CLIENT = new PostgresClient(
            String.format("jdbc:postgresql://db:5432/%s", System.getenv("POSTGRES_DB")),
            System.getenv("POSTGRES_USER"),
            System.getenv("POSTGRES_PASSWORD")
    );

    /**
     * REPOSITORIES
     */
    static final UserRepository USER_REPOSITORY = new UserRepository(POSTGRESQL_CLIENT);
    static final MessageRepository MESSAGE_REPOSITORY = new MessageRepository(POSTGRESQL_CLIENT);
    private static final MessageService MESSAGE_SERVICE = new MessageService(MESSAGE_REPOSITORY);
    static final ShutdownHook SHUTDOWN_HOOK = new ShutdownHook();
    /**
     * MAPPERS
     */
    private static final JsonMapper JSON_MAPPER = new JsonMapper();
    private static final QueryMapper QUERY_MAPPER = new QueryMapper();
    private static final AuthMapper AUTH_MAPPER = new AuthMapper();
    private static final UserMapper USER_MAPPER = new UserMapper(JSON_MAPPER);
    private static final MessageMapper MESSAGE_MAPPER = new MessageMapper(JSON_MAPPER, QUERY_MAPPER);
    static final MessageController MESSAGE_CONTROLLER = new MessageController(MESSAGE_MAPPER, MESSAGE_SERVICE);
    /**
     * SERVICES
     */
    private static final ReaderService READER_SERVICE = new ReaderService();
    /**
     * CONTROLLERS
     */
    static final PublicController PUBLIC_CONTROLLER = new PublicController(READER_SERVICE);
    static final PageController PAGE_CONTROLLER = new PageController(READER_SERVICE);
    private static final HashingService HASHING_SERVICE = new HashingService();
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY, HASHING_SERVICE);
    static final UserController USER_CONTROLLER = new UserController(AUTH_MAPPER, JSON_MAPPER, USER_MAPPER, USER_SERVICE);
    /**
     * OTHERS
     */
    static final DemoDataProvider DEMO_DATA_PROVIDER = new DemoDataProvider(USER_SERVICE, MESSAGE_SERVICE, READER_SERVICE);
    public static final AuthService AUTH_SERVICE = new AuthService(HASHING_SERVICE, USER_REPOSITORY);
    static final AuthController AUTH_CONTROLLER = new AuthController(AUTH_MAPPER, USER_MAPPER, AUTH_SERVICE);
}
