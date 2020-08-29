package com.harnet.locationtest.dao;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface PlaceDao<Place> extends Dao<Place> {
    /**
     * Add a new object to database, and set the new ID
     *
     * @param name a name of new object
     * @param latLng are place coordinates
     */
    void add(String name, LatLng latLng);

    /**
     * Update existing object's data in the database
     *
     * @param placeForUpdate an object from the database, with ID already set
     */
    //TODO think is id necessary
    void update(Place placeForUpdate, int id);

    /**
     * check if the place in places repository already
     *
     * @return boolean
     */
    boolean isPlaceInPlaces(List<Place> places, LatLng placeCoords);
}
