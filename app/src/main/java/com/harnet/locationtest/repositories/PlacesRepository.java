package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.models.UserCoords;

import java.util.ArrayList;
import java.util.List;

public class PlacesRepository {
    private static PlacesRepository instance = null;
    private List<Place> placesDataSet = new ArrayList<>();

    private PlacesRepository() {
    }

    public static PlacesRepository getInstance() {
        if(instance == null){
            instance = new PlacesRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Place>> getUsersDataSet() {
        MutableLiveData<List<Place>> data = new MutableLiveData<>();
        data.setValue(placesDataSet);
        return data;
    }
}
