package com.harnet.locationtest.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.dao.PlaceDaoDatabase;
import com.harnet.locationtest.dao.PlaceDaoInMemory;
import com.harnet.locationtest.models.Place;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesService {
    private static PlacesService instance;
    private Context context;

    private PlaceDaoInMemory placeDaoInMemory = new PlaceDaoInMemory();
    private PlaceDaoDatabase placeDaoDatabase;

    private ObjectSerializeService objectSerializeService = new ObjectSerializeService();

    private SharedPreferencesService sharedPreferencesService = new SharedPreferencesService("favouritePlaces", objectSerializeService);

    private PlacesService(Context context) {
        this.context = context;
        placeDaoDatabase = new PlaceDaoDatabase(context);
    }

    public static PlacesService getInstance(Context context) {
        if (instance == null) {
            instance = new PlacesService(context);
        }
        return instance;
    }

    public MutableLiveData<List<Place>> getmPlacesRepository(){
        return placeDaoInMemory.getmPlaces();
    }

    public ObjectSerializeService getObjectSerializeService() {
        return objectSerializeService;
    }

    // add new place to places with QR code or manually by Google maps
    //TODO make all cases of using this method get Place as argument
    public boolean addNewPlace(String name, LatLng latLng) {
        if (!placeDaoInMemory.isPlaceInPlaces(placeDaoInMemory.getAll(), latLng)) {
            placeDaoInMemory.add(new Place(name, latLng.latitude, latLng.longitude));
            return true;
        }
        // TODO think about changing boolean to Exceptions
        return false;
    }

    // adding functionality for retrieving
    public boolean addNewPlace(Place place) {
        // if place doesn't exist in Places List
        if (place != null && !placeDaoInMemory.isPlaceInPlaces(placeDaoInMemory.getAll(), new LatLng(place.getLat(), place.getLng()))) {
            placeDaoInMemory.add(place);
            return true;
        }
        return false;
    }

    // delete place from favourite places
    public boolean deletePlace(Place placeForDelete) {
        if (placeForDelete != null && placeDaoInMemory.isPlaceInPlaces(placeDaoInMemory.getAll(), new LatLng(placeForDelete.getLat(), placeForDelete.getLng()))) {
            placeDaoInMemory.delete(placeForDelete);
            return true;
        }
        return false;
    }

    // edit place fields
    public boolean editPlace(Place placeForEdit) {
        if (placeForEdit != null && placeDaoInMemory.isPlaceInPlaces(placeDaoInMemory.getAll(), new LatLng(placeForEdit.getLat(), placeForEdit.getLng()))) {
            placeDaoInMemory.update(placeForEdit, placeForEdit.getId());
            return true;
        }
        return false;
    }

    public List<Place> getFavouritePlaces(){
        return placeDaoInMemory.getAll();
    }

    //TODO SharedPreferences functionality

    // save places to SharedPreferences
    public void saveToSharedPref() throws IOException {
        sharedPreferencesService.saveToSharedPref(context, placeDaoInMemory.getAll());
    }

    // retrieve places from SharedPreferences and fill places List
    public void retrieveFromSharedPref() throws IOException {
        sharedPreferencesService.retrieveFromSharedPref(context);
    }

    // check is place in favourite places
    public boolean isPlacesInSharedPref() throws IOException {
        return sharedPreferencesService.isPlacesInSharedPref(context);
    }

    //TODO SQLite

    // add place to SQLite database
    public void addPlaceToDatabase(Place place) throws SQLException {
        placeDaoDatabase.add(place);
    }
}