/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;

import com.sun.net.httpserver.Headers;
import java.security.SecureRandom;
import java.util.Objects;

public class HashingService {
    private static final SecureRandom RANDOM = new SecureRandom();

    public boolean authorize(Headers headers) {
        return true; // todo
    }

    public String hash(String toHash) {
        return toHash;
       /* byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(toHash.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Arrays.toString(factory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }*/
    }

    public boolean compare(String plain, String hashed) {
        return Objects.equals(plain, hashed);
        // return hash(plain).equals(hashed);
    }
}
