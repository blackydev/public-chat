/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import static com.patryklikus.publicchat.config.BeanProvider.*;

import com.patryklikus.publicchat.https.RequestHandlersManager;
import com.sun.net.httpserver.HttpServer;
import java.sql.SQLException;

public class BeanInitializer {
    public static void initBeans(HttpServer server) throws SQLException {
        initRepositories();
        initEndpoints(server);
    }

    private static void initRepositories() throws SQLException {
        POSTGRESQL_CLIENT.connect();
        USER_REPOSITORY.createTable();
        MESSAGE_REPOSITORY.createTable();
        DEMO_DATA_PROVIDER.init();
    }

    private static void initEndpoints(HttpServer server) {
        var requestHandlersManager = new RequestHandlersManager(server, AUTH_SERVICE);
        requestHandlersManager.addControllers(
                PUBLIC_CONTROLLER, PAGE_CONTROLLER, USER_CONTROLLER, MESSAGE_CONTROLLER, AUTH_CONTROLLER
        );
        requestHandlersManager.init();
    }
}
