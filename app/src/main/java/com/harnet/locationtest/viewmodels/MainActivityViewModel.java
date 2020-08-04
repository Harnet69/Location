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

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private UserCoordsRepository mUsersRepository;

    private LocationService locationService;

    private MutableLiveData<List<UserCoords>> mUsers;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public LocationService getLocationService() {
        return locationService;
    }

    public void init(Context context, Activity activity) {
        if (mUsers != null) {
            return;
        }

        mUsersRepository = UserCoordsRepository.getInstance();
        mUsers = mUsersRepository.getUsersDataSet();

        // location service starts automatically
        locationService = new LocationService(context, activity, this);
        // location
        getLastKnownLocation();
    }

    public LiveData<List<UserCoords>> getmPersons() {
        return mUsers;
    }

    public LiveData<Boolean> getmIsUpdated() {
        return mIsUpdating;
    }



    @SuppressLint("StaticFieldLeak")
    public void changeUserCoords(final double lat, final double lng) {
        mIsUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                List<UserCoords> currentPlaces = mUsers.getValue();
                assert currentPlaces != null;
                currentPlaces.get(0).setLat(lat);
                currentPlaces.get(0).setLng(lng);
                mUsers.postValue(currentPlaces);
//                mIsUpdating.postValue(false);
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

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        Location lastKnownLocation = null;
        if (locationService != null) {
            lastKnownLocation = locationService.getLocationManager().getLastKnownLocation(locationService.getProvider());
            Log.i("TestLoc:", "Last known location: " + lastKnownLocation);
        }

        if (lastKnownLocation != null) {
            mUsersRepository.initiateUser(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        }
    }
}