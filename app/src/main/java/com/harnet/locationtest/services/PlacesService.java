package com.harnet.locationtest.services;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.repositories.PlacesRepository;

import java.util.List;

public class PlacesService {
    private static PlacesService instance;
    private PlacesRepository mPlacesRepository;

    private MutableLiveData<List<Place>> mPlaces;

    private PlacesService() {

        mPlacesRepository = PlacesRepository.getInstance();
        mPlaces = mPlacesRepository.getUsersDataSet();
    }

    public static PlacesService getInstance() {
        if(instance == null){
            instance = new PlacesService();
        }
        return instance;
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
}
