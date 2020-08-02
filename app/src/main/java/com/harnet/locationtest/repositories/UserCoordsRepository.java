package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.UserCoords;

import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
    private static UsersRepository instance = null;
    private List<UserCoords> usersDataSet = new ArrayList<>();

    private UsersRepository() {
    }

    public static UsersRepository getInstance() {
        if(instance == null){
            instance = new UsersRepository();
        }
        return instance;
    }

    public MutableLiveData<List<UserCoords>> getUsersDataSet() {
        MutableLiveData<List<UserCoords>> data = new MutableLiveData<>();
        data.setValue(usersDataSet);
        return data;
    }
}
