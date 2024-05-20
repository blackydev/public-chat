/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat;


import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class ShutdownHook extends Thread {
    private static final Logger LOG = Logger.getLogger(ShutdownHook.class.getName());
    private List<Closeable> closeables = new LinkedList<>();

    @Override
    public void run() {
        closeables.forEach(this::closeSafe);
    }

    public void add(Closeable closeable) {
        closeables.add(closeable);
    }

    private void closeSafe(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            LOG.warning("Invalid shutdown: " + e.getMessage());
        }
    }
}
