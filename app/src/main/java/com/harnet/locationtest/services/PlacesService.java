package com.harnet.locationtest.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.repositories.PlacesRepository;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlacesService {
    private static PlacesService instance;

    private PlacesRepository mPlacesRepository;

    private ObjectSerializeService objectSerializeService = new ObjectSerializeService();

    private MutableLiveData<List<Place>> mPlaces;

    private PlacesService() {
        mPlacesRepository = new PlacesRepository();
        mPlaces = mPlacesRepository.getUsersDataSet();
    }

    public static PlacesService getInstance() {
        if(instance == null){
            instance = new PlacesService();
        }

        return instance;
    }

    public PlacesRepository getmPlacesRepository() {
        return mPlacesRepository;
    }


    public ObjectSerializeService getObjectSerializeService() {
        return objectSerializeService;
    }

    // add new place to places
    public boolean addNewPlace(String name, LatLng latLng) {
        Place newPlace = new Place(name, latLng.latitude, latLng.longitude);
        List<Place> currentPlaces = mPlaces.getValue();
        // if place doesn't exist in Places List
        if (currentPlaces != null && !isPlaceInPlaces(currentPlaces, latLng)) {
            currentPlaces.add(newPlace);
            mPlaces.postValue(currentPlaces);
            return true;
        }

        return false;
    }

    // delete place from favourite places
    public boolean deletePlace(Place placeForDelete){
        List<Place> currentPlaces = mPlaces.getValue();

        if (currentPlaces.size() > 0 && isPlaceInPlaces(currentPlaces, new LatLng(placeForDelete.getLat(), placeForDelete.getLng()))) {
            currentPlaces.remove(placeForDelete);
            mPlaces.postValue(currentPlaces);

            return true;
        }

        return false;
    }

    // check if the place in places repository already
    private boolean isPlaceInPlaces(List<Place> places, LatLng placeCoords) {
        for (Place place : places) {
            if (placeCoords.latitude == place.getLat() && placeCoords.longitude == place.getLng()) {
                return true;
            }
        }

        return false;
    }

    // save places to SharedPreferences
    public void saveToSharedPref(Context context, List<Place> places) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("lovedPlaces", objectSerializeService.serialize((Serializable) getmPlacesRepository().getPlacesDataSet())).apply();
    }

    // retrieve places from SharedPreferences and fill Places List
    public void retrieveFromSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);
        List<Place> retrievedPlaces = (List<Place>) objectSerializeService.deserialize(sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<Place>())));
        PlacesService.getInstance().getmPlacesRepository().getPlacesDataSet().clear(); // clear favorite places before retrieving
        for (Place place : retrievedPlaces) {
            PlacesService.getInstance().addNewPlace(place.getName(), new LatLng(place.getLat(), place.getLng()));
        }
    }

    public boolean isPlacesInSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);

        return sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<String>())) != null;
    }
}