package com.harnet.locationtest.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.dao.PlaceDaoInMemory;
import com.harnet.locationtest.models.Place;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesService {
    private final String SHARED_PREFERENCES_NAME = "com.harnet.sharedpreferences";
    private static PlacesService instance;

    private PlaceDaoInMemory placeDaoInMemory = new PlaceDaoInMemory();

    private ObjectSerializeService objectSerializeService = new ObjectSerializeService();

    private PlacesService() {
    }

    public static PlacesService getInstance() {
        if (instance == null) {
            instance = new PlacesService();
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
        Place newPlace = new Place(name, latLng.latitude, latLng.longitude);
        if (!placeDaoInMemory.isPlaceInPlaces(placeDaoInMemory.getAll(), latLng)) {
            placeDaoInMemory.add(newPlace);
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
    public void saveToSharedPref(Context context, List<Place> places) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("lovedPlaces", objectSerializeService.serialize((Serializable) placeDaoInMemory.getAll())).apply();
    }

    // retrieve places from SharedPreferences and fill places List
    public void retrieveFromSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Object retrievedPlacesObject = objectSerializeService.deserialize(sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<Place>())));
        List<Place> retrievedPlaces = null;
        try {
            retrievedPlaces = Collections.unmodifiableList((List<Place>) retrievedPlacesObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // record all favourite places to SHaredPreferences
        if (retrievedPlaces != null && retrievedPlaces.size() > 0) {
            for (Place place : retrievedPlaces) {
                PlacesService.getInstance().addNewPlace(place);
            }
        }
    }

    // check is place in favourites
    public boolean isPlacesInSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<String>())) != null;
    }
}