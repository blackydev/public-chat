/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.controllers.PageController;
import com.patryklikus.publicchat.controllers.PublicController;
import com.patryklikus.publicchat.services.ReaderService;

public class BeanProvider {
    private static final ReaderService READER_SERVICE = new ReaderService();
    private static final PublicController PUBLIC_CONTROLLER = new PublicController(READER_SERVICE);
    private static final PageController PAGE_CONTROLLER = new PageController(READER_SERVICE);
    private static final PostgresClient POSTGRESQL_CLIENT = new PostgresClient("jdbc:postgresql://localhost:5432/db", "wdpai", "password");

    public static ReaderService getReaderService() {
        return READER_SERVICE;
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
