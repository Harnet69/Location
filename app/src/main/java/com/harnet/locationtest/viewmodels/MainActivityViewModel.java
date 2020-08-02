package com.harnet.locationtest.viewmodels;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.repositories.UserCoordsRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<UserCoords>> mUsers;
    private UserCoordsRepository mUsersRepository;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init(){
        if(mUsers != null){
            return;
        }

        mUsersRepository = UserCoordsRepository.getInstance();
        mUsers = mUsersRepository.getUsersDataSet();
    }

    public LiveData<List<UserCoords>> getmPersons() {
        return mUsers;
    }

    @SuppressLint("StaticFieldLeak")
    public void changeUserCoords(final double lat, final double lng){
            mIsUpdating.setValue(true);

            new AsyncTask<Void, Void, Void>(){
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    List<UserCoords> currentPlaces = mUsers.getValue();
                    assert currentPlaces != null;
                    currentPlaces.get(0).setLat(lat);
                    currentPlaces.get(0).setLng(lng);
                    mUsers.postValue(currentPlaces);
                    mIsUpdating.postValue(false);
                }

                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

}
