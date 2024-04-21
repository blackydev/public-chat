/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat;

import com.patryklikus.publicchat.config.BeanInitializer;
import com.patryklikus.publicchat.https.request.RequestHandlersManager;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Launcher {
    private static final Logger LOG = Logger.getLogger(RequestHandlersManager.class.getName());
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, SQLException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
        HttpServer server = HttpServer.create(inetSocketAddress, 0);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 2_000, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), Thread.ofVirtual().factory());
        server.setExecutor(threadPoolExecutor);

        BeanInitializer.initBeans(server);

        LOG.info("Running server...");
        server.start();
        LOG.info("Server has been started on port " + PORT);
    }
}