/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat;

import com.patryklikus.publicchat.config.BeanProvider;
import com.patryklikus.publicchat.engine.https.request.RequestHandlersManager;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Launcher {
    private static final Logger LOG = Logger.getLogger(RequestHandlersManager.class.getName());
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
        HttpServer server = HttpServer.create(inetSocketAddress, 0); // todo change to Https

        RequestHandlersManager requestHandlersManager = new RequestHandlersManager(server);
        requestHandlersManager.addController(BeanProvider.getPublicController());
        requestHandlersManager.addController(BeanProvider.getPageController());
        requestHandlersManager.init();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 2_000, 60, TimeUnit.SECONDS, new SynchronousQueue<>(), Thread.ofVirtual().factory());
        server.setExecutor(threadPoolExecutor);
        LOG.info("Running server...");
        server.start(); // todo handle closing
        LOG.info("Server has been started on port " + PORT);
    }
}