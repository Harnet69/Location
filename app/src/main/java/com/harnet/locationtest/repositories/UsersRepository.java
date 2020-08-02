package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
    private static UsersRepository instance = null;
    private List<User> userDataSet = new ArrayList<>();

    private UsersRepository() {
    }

    public static UsersRepository getInstance() {
        if(instance == null){
            instance = new UsersRepository();
        }
        return instance;
    }

    public MutableLiveData<List<User>> getPersonsDataSet() {
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.setValue(userDataSet);
        return data;
    }
}
