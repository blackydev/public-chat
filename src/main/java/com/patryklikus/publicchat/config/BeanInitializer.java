/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.https.RequestHandlersManager;
import com.sun.net.httpserver.HttpServer;

import java.sql.SQLException;

public class BeanInitializer {
    public static void initBeans(HttpServer server) throws SQLException {
        initRepositories();
        initEndpoints(server);
    }

    private static void initRepositories() throws SQLException {
        BeanProvider.getPostgresqlClient().connect();
        BeanProvider.getUserRepository().createTable();
    }

    private static void initEndpoints(HttpServer server) {
        var requestHandlersManager = new RequestHandlersManager(server, BeanProvider.getAuthService());
        requestHandlersManager.addControllers(
                BeanProvider.getPublicController(),
                BeanProvider.getPageController(),
                BeanProvider.getUserController()
        );
        requestHandlersManager.init();
    }
}
