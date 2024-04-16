package com.patryklikus.publicchat.repositories;

import java.sql.SQLException;

public interface Repository<T> {
    T findById(Long id) throws SQLException;

    /**
     * Creates/updates object.
     */
    T save(T obj) throws SQLException;

    /**
     * Removes object.
     */
    void remove(T obj) throws SQLException;
}
