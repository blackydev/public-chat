/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.config;

import com.patryklikus.publicchat.controllers.PublicController;
import com.patryklikus.publicchat.services.PublicService;

public class BeanProvider {
    private static final PublicService PUBLIC_SERVICE = new PublicService();
    private static final PublicController PUBLIC_CONTROLLER = new PublicController(PUBLIC_SERVICE);

    public static PublicService getPublicService() {
        return PUBLIC_SERVICE;
    }

    public static PublicController getPublicController() {
        return PUBLIC_CONTROLLER;
    }
}
