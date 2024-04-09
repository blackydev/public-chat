/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat;

import com.patryklikus.publicchat.config.BeanProvider;
import com.patryklikus.publicchat.https.request.RequestHandlersManager;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Launcher {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8080);
        HttpServer server = HttpServer.create(inetSocketAddress, 0); // todo change to Https

        RequestHandlersManager requestHandlersManager = new RequestHandlersManager(server);
        requestHandlersManager.addController(BeanProvider.getPublicController());

        server.setExecutor(null);
        System.out.println("Running server...");
        server.start();
        System.out.println("Server has been started");
    }
}