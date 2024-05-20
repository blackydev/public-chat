/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.services;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashingService {
    private static final String SALT_SEPARATOR = "\u0007";

    /**
     * @return salt \u0007 hash
     */
    public String hash(String toHash) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        return String.format("%s%s%s", encodedSalt, SALT_SEPARATOR, hash(toHash, salt));
    }

    public boolean compare(String plain, String hashed) {
        String[] saltAndHash = hashed.split(SALT_SEPARATOR);
        byte[] salt = Base64.getDecoder().decode(saltAndHash[0]);
        return Objects.equals(hash(plain, salt), saltAndHash[1]);
    }

    /**
     * @return hash
     */
    private String hash(String toHash, byte[] salt) {
        KeySpec spec = new PBEKeySpec(toHash.toCharArray(), salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return new String(factory.generateSecret(spec).getEncoded());
        } catch (NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
