package com.harnet.locationtest.dao;

import com.harnet.locationtest.models.Place;

import java.sql.SQLException;
import java.util.List;

public interface PlaceDao {
    /**
     * Add a new object to database, and set the new ID
     *
     * @param place a new object, with ID not set yet (null)
     */
    void add(Place place) throws SQLException;

    /**
     * Update existing object's data in the database
     *
     * @param place an object from the database, with ID already set
     */
    void update(Place place, int id) throws SQLException;

    /**
     * Get object by ID
     *
     * @param id ID to search by
     * @return Object with a given ID, or null if not found
     */
    Place get(int id) throws SQLException;

    /**
     * Get all objects
     *
     * @return List of all objects of this type in the database
     */
    List<Place> getAll() throws SQLException;
}
