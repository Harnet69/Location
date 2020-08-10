package com.harnet.locationtest.viewmodels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.repositories.UserCoordsRepository;
import com.harnet.locationtest.services.LocationService;
import com.harnet.locationtest.services.SoundService;

import java.util.List;

public class LocationActivityViewModel extends ViewModel {
    private UserCoordsRepository mUsersRepository;

    private LocationService locationService;
    private SoundService soundService;

    private MutableLiveData<List<UserCoords>> mUsers;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public LocationService getLocationService() {
        return locationService;
    }

    public SoundService getSoundService() {
        return soundService;
    }

    public void init(Context context, Activity activity) {
        if (mUsers != null) {
            return;
        }

        mUsersRepository = UserCoordsRepository.getInstance();
        mUsers = mUsersRepository.getUsersDataSet();

        // location service starts automatically
        locationService = new LocationService(context, activity, this);

        // sounds
        soundService = new SoundService(context);

        // last known location
        Location lastKnownLocation = locationService.getLastKnownLocation();
        if (lastKnownLocation != null) {
            mUsersRepository.initiateUser(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAltitude());
        }
    }

    public LiveData<List<UserCoords>> getmPersons() {
        return mUsers;
    }

    public LiveData<Boolean> getmIsUpdated() {
        return mIsUpdating;
    }

    @SuppressLint("StaticFieldLeak")
    public void changeUserCoords(final double lat, final double lng, final double alt) {
        mIsUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                List<UserCoords> currentPlaces = mUsers.getValue();
                if(currentPlaces != null && currentPlaces.size() > 0){
                    currentPlaces.get(0).setLat(lat);
                    currentPlaces.get(0).setLng(lng);
                    currentPlaces.get(0).setAlt(alt);
                    mUsers.postValue(currentPlaces);
                    mIsUpdating.postValue(false);
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }
}