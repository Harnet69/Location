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
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        if (instance == null) {
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
    public boolean deletePlace(Place placeForDelete) {
        List<Place> currentPlaces = mPlaces.getValue();

        if (currentPlaces.size() > 0 && isPlaceInPlaces(currentPlaces, new LatLng(placeForDelete.getLat(), placeForDelete.getLng()))) {
            currentPlaces.remove(placeForDelete);
            mPlaces.postValue(currentPlaces);

            return true;
        }

        return false;
    }

    public boolean editPlace(Place placeForEdit) {
        List<Place> currentPlaces = mPlaces.getValue();
        //TODO edit functionality
        if (currentPlaces != null && currentPlaces.size() > 0 && placeForEdit != null) {

            for (Place place : currentPlaces) {
                if (placeForEdit.getLat() == place.getLat() && placeForEdit.getLng() == place.getLng()) {

                    // if place was edited
//                    if (place.getDescription() != null || !placeForEdit.getName().equals(place.getName()) ||
//                            !placeForEdit.getDescription().equals(place.getDescription()) ||
//                            placeForEdit.getImage() != place.getImage()) {
                        //TODO implement verification if changes were done
                    place.setName(placeForEdit.getName());
                    place.setDescription(placeForEdit.getDescription());

                        return true;
//                    }

                } else {
                    throw new NoSuchElementException();
                }
            }

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

    private Place getPlaceFromFavourite(List<Place> favPlaces, LatLng placeCoords) {
        return favPlaces.stream()
                .filter(x -> x.getLat() == placeCoords.latitude)
                .filter(x -> x.getLng() == placeCoords.longitude)
                .collect(Collectors.toList()).get(0);
    }

    // save places to SharedPreferences
    public void saveToSharedPref(Context context, List<Place> places) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("lovedPlaces", objectSerializeService.serialize((Serializable) getmPlacesRepository().getPlacesDataSet())).apply();
    }

    // retrieve places from SharedPreferences and fill Places List
    public void retrieveFromSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);
        Object retrievedPlacesObject = objectSerializeService.deserialize(sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<Place>())));
        List<Place> retrievedPlaces = null;
        try{
            retrievedPlaces = Collections.unmodifiableList((List<Place>) retrievedPlacesObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(retrievedPlaces != null && retrievedPlaces.size() > 0) {
            for (Place place : retrievedPlaces) {
                PlacesService.getInstance().addNewPlace(place.getName(), new LatLng(place.getLat(), place.getLng()));
            }
        }
    }

    public boolean isPlacesInSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.harnet.sharedpreferences", Context.MODE_PRIVATE);

        return sharedPreferences.getString("lovedPlaces", objectSerializeService.serialize(new ArrayList<String>())) != null;
    }
}