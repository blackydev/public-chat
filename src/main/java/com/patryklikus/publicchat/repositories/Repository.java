/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;


public interface Repository<T> {
    T findById(Long id);

    /**
     * Creates/updates object.
     */
    T save(T obj);

    /**
     * Removes object.
     */
    void remove(T obj);
}
