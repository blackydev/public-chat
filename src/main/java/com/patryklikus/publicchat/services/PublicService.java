/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.patryklikus.publicchat.controllers.PublicController;
import java.io.InputStream;
import java.util.Scanner;

public class PublicService {
    private static final ClassLoader CLASS_LOADER = PublicController.class.getClassLoader();

    /**
     * @return resource content or null if doesn't exist
     */
    public String getPublicResource(String resourceUri) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = CLASS_LOADER.getResourceAsStream(resourceUri)) {
            if (inputStream == null) {
                return null;
            }
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            while (scanner.hasNextLine()) {
                content.append("\n").append(scanner.nextLine());
            }
        } catch (Exception e) {
            return null;
        }
        return content.toString();
    }
}
