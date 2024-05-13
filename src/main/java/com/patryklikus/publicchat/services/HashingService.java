/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;


import java.security.SecureRandom;
import java.util.Objects;

public class HashingService {
    private static final SecureRandom RANDOM = new SecureRandom();

    public String hash(String toHash) {
        return toHash;
    }

    public boolean compare(String plain, String hashed) {
        return Objects.equals(plain, hashed);
    }
}
