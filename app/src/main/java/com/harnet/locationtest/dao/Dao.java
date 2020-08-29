package com.harnet.locationtest.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    /**
     * Add a new object to database, and set the new ID
     *
     * @param item a new object, with ID not set yet (null)
     */
    void add(T item) throws SQLException;

    /**
     * Update existing object's data in the database
     *
     * @param item an object from the database, with ID already set
     */
    void update(T item, int id) throws SQLException;

    /**
     * Get object by ID
     *
     * @param id ID to search by
     * @return Object with a given ID, or null if not found
     */
    T get(int id) throws SQLException;

    /**
     * Get all objects
     *
     * @return List of all objects of this type in the database
     */
    List<T> getAll() throws SQLException;
}
