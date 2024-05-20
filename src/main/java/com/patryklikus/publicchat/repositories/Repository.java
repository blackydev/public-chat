/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.repositories;


import com.patryklikus.publicchat.models.Idable;

public interface Repository<T extends Idable> {
    /**
     * Creates/updates object.
     */
    void save(T obj);

    /**
     * Removes object with provided ID.
     */
    void remove(long id);

    /**
     * Removes object.
     */
    default void remove(T obj) {
        remove(obj.getId());
    }
}
