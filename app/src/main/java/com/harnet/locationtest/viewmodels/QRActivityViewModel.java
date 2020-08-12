package com.harnet.locationtest.viewmodels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.repositories.PlacesRepository;
import com.harnet.locationtest.repositories.UserCoordsRepository;
import com.harnet.locationtest.services.CameraService;

import java.util.List;

public class QRActivityViewModel extends ViewModel {
    private PlacesRepository mPlacesRepository;

    private CameraService cameraService;

    private MutableLiveData<List<Place>> mPlaces;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public CameraService getCameraService() {
        return cameraService;
    }

    public void init(Context context, Activity activity) {
        if (mPlaces != null) {
            return;
        }

        mPlacesRepository = PlacesRepository.getInstance();
        mPlaces = mPlacesRepository.getUsersDataSet();

        cameraService = new CameraService(context, activity);
    }

    public LiveData<List<Place>> getmPlaces() {
        return mPlaces;
    }

    public LiveData<Boolean> getmIsUpdated() {
        return mIsUpdating;
    }

    //TODO add method for adding coordinates to placesRepository
}
