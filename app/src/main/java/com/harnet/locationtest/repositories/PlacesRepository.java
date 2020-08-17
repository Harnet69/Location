package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.Place;

import java.util.ArrayList;
import java.util.List;

public class PlacesRepository {
    private static PlacesRepository instance = null;
    private List<Place> placesDataSet = new ArrayList<>();

    private PlacesRepository() {
    }

    //TODO check if something is in SharedPreferences and if it is - add it to placesDataSet
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
