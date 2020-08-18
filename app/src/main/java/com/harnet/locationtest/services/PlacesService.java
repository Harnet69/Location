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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // add new place to places
    public boolean addNewPlace(String name, LatLng latLng) {
        Place newPlace = new Place(name, latLng.latitude, latLng.longitude);
        List<Place> currentPlaces = mPlaces.getValue();

        if (currentPlaces != null && !isPlaceInPlaces(currentPlaces, latLng)) {
            currentPlaces.add(newPlace);
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

    // retrieve places from SharedPreferences
    public void retrieveFromSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);
        List<Place> retrievedPlaces = (List<Place>) objectSerializeService.deserialize(sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<String>())));
        for (Place place : retrievedPlaces) {
            PlacesService.getInstance().addNewPlace(place.getName(), new LatLng(place.getLat(), place.getLng()));
        }
    }

    public boolean isPlacesInSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);
        Log.i("Retrieved", "isPlacesInSharedPref: " + sharedPreferences);
        return sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<String>())) != null;
    }
}