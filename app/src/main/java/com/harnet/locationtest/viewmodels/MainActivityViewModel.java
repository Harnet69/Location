package com.harnet.locationtest.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.harnet.locationtest.models.User;
import com.harnet.locationtest.repositories.UsersRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<User>> mUsers;
    private UsersRepository mUsersRepository;

    public void init(){
        if(mUsers != null){
            return;
        }

        mUsersRepository = UsersRepository.getInstance();
        mUsers = mUsersRepository.getUsersDataSet();
    }

    public LiveData<List<User>> getmPersons() {
        return mUsers;
    }
}
