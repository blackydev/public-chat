/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat;

import com.patryklikus.publicchat.clients.PostgresClient;
import com.patryklikus.publicchat.config.BeanProvider;
import com.patryklikus.publicchat.https.request.RequestHandlersManager;
import com.patryklikus.publicchat.models.User;
import com.patryklikus.publicchat.repositories.UserRepository;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.patryklikus.publicchat.models.UserBuilder.anUser;

public class Launcher {
    private static final Logger LOG = Logger.getLogger(RequestHandlersManager.class.getName());
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, SQLException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
        HttpServer server = HttpServer.create(inetSocketAddress, 0);

        PostgresClient postgresClient = BeanProvider.getPostgresqlClient();
        postgresClient.connect();
        postgresClient.initDatabase();

        RequestHandlersManager requestHandlersManager = new RequestHandlersManager(server);
        requestHandlersManager.addController(BeanProvider.getPublicController());
        requestHandlersManager.addController(BeanProvider.getPageController());
        requestHandlersManager.init();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 2_000, 60, TimeUnit.SECONDS, new SynchronousQueue<>(), Thread.ofVirtual().factory());
        server.setExecutor(threadPoolExecutor);
        LOG.info("Running server...");
        server.start();
        LOG.info("Server has been started on port " + PORT);

        var userRepository = new UserRepository(postgresClient);
        User user = anUser().withIsAdmin(false)
                .withUsername("Ekko")
                .withEmail("email@gmail.com")
                .withPassword("Password123")
                .build();
        userRepository.create(user);
    }
}