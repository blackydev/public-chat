/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Launcher {
    public static void main(String[] args) throws IOException {
        var inetSocketAddress = new InetSocketAddress(8080);
        HttpServer server = HttpServer.create(inetSocketAddress, 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "This is the response";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}