/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;


public interface Repository<T> {
    /**
     * Creates/updates object.
     */
    void save(T obj);

    /**
     * Removes object.
     */
    void remove(T obj);
}
