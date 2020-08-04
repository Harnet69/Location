package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.UserCoords;

import java.util.ArrayList;
import java.util.List;


public class UserCoordsRepository {
    private static UserCoordsRepository instance = null;
    private List<UserCoords> usersDataSet = new ArrayList<>();

    private UserCoordsRepository() {
    }

    public void initiateUser(double lat, double lng, double alt){
        usersDataSet.add(new UserCoords(lat, lng, alt));
    }

    public static UserCoordsRepository getInstance() {
        if(instance == null){
            instance = new UserCoordsRepository();
        }
        return instance;
    }

    public MutableLiveData<List<UserCoords>> getUsersDataSet() {
        MutableLiveData<List<UserCoords>> data = new MutableLiveData<>();
        data.setValue(usersDataSet);
        return data;
    }
}