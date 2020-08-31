package com.harnet.locationtest.dao;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.repositories.PlacesRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceDaoInMemory implements PlaceDao<Place> {

    private PlacesRepository mPlacesRepository;
    private MutableLiveData<List<Place>> mPlaces;

    public PlaceDaoInMemory() {
        this.mPlacesRepository = new PlacesRepository();
        mPlaces = mPlacesRepository.getUsersDataSet();
    }

    public MutableLiveData<List<Place>> getmPlaces() {
        return mPlaces;
    }

    @Override
    public void add(String name, LatLng latLng) {
        Place newPlace = new Place(name, latLng.latitude, latLng.longitude);
        List<Place> currentPlaces = mPlaces.getValue();

        // if place doesn't exist in Places List
        if (currentPlaces != null && !isPlaceInPlaces(currentPlaces, latLng)) {
            currentPlaces.add(newPlace);
            mPlaces.postValue(currentPlaces);
        }
    }

    @Override
    public void add(Place item){
        List<Place> places = mPlaces.getValue();
        // if place doesn't exist in Places List
        if (places != null && !isPlaceInPlaces(places, new LatLng(item.getLat(), item.getLng()))) {
            places.add(item);
            mPlaces.postValue(places);
        }
    }

    @Override
    public void update(Place placeForEdit, int id) {
        List<Place> currentPlaces = mPlaces.getValue();

        if (currentPlaces != null && currentPlaces.size() > 0 && placeForEdit != null) {

            for (Place place : currentPlaces) {
                if (placeForEdit.getLat() == place.getLat() && placeForEdit.getLng() == place.getLng()) {

                    //TODO implement image changing
                    place.setName(placeForEdit.getName());
                    place.setDescription(placeForEdit.getDescription());
                    // save changes in repository
                    mPlaces.postValue(currentPlaces);
                }
            }
        }
    }

    @Override
    public void delete(Place item){
        List<Place> places = mPlaces.getValue();

        if (places != null && places.size() > 0 && isPlaceInPlaces(places, new LatLng(item.getLat(), item.getLng()))) {
            places.remove(item);
            mPlaces.postValue(places);
        }
    }

    @Override
    public Place get(int id){
        List<Place> places = mPlaces.getValue();

        if (places != null) {
            return places.stream()
                    .filter(x -> x.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public List<Place> getAll(){
        return mPlaces.getValue();
    }

    @Override
    public boolean isPlaceInPlaces(List<Place> places, LatLng placeCoords) {
        for (Place place : places) {
            if (placeCoords.latitude == place.getLat() && placeCoords.longitude == place.getLng()) {
                return true;
            }
        }
        return false;
    }

    //clear places in memory
    public void clearPlacesInMemory() {
        mPlaces.postValue(new ArrayList<>());
    }
}