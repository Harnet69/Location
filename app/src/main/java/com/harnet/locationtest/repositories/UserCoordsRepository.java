package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.UserCoords;

import java.util.ArrayList;
import java.util.List;


public class UserCoordsRepository {
    private static UserCoordsRepository instance = null;
    private List<UserCoords> usersDataSet = new ArrayList<>();

    private UserCoordsRepository() {
        // TODO think about getting last known coordinates from mainActivity
        setUser(20,40);
    }

    private void setUser(double lat, double lng){
        usersDataSet.add(new UserCoords(lat, lng));
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
