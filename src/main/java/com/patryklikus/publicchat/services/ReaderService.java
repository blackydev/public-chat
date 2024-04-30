/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import java.io.InputStream;
import java.util.Scanner;

public class ReaderService {
    private static final ClassLoader CLASS_LOADER = ReaderService.class.getClassLoader();

    /**
     * @return resource content or empty string if doesn't exist
     */
    public String readResource(String resourceUri) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = CLASS_LOADER.getResourceAsStream(resourceUri)) {
            if (inputStream == null) {
                return "";
            }
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNextLine()) {
                content.append(scanner.nextLine());
            }
            while (scanner.hasNextLine()) {
                content.append("\n").append(scanner.nextLine());
            }
        } catch (Exception e) {
            return "";
        }
        return content.toString();
    }
}
