package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.Place;

import java.util.ArrayList;
import java.util.List;

public class PlacesRepository {
    private List<Place> placesDataSet = new ArrayList<>();

    public PlacesRepository() {
    }

    public List<Place> getPlacesDataSet() {
        return placesDataSet;
    }

    public MutableLiveData<List<Place>> getUsersDataSet() {
        MutableLiveData<List<Place>> data = new MutableLiveData<>();
        data.setValue(placesDataSet);
        return data;
    }
}
