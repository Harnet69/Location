package com.harnet.locationtest.viewmodels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.repositories.PlacesRepository;
import com.harnet.locationtest.services.CameraService;

import java.util.List;

public class QRActivityViewModel extends ViewModel {
    private PlacesRepository mPlacesRepository;

    private CameraService cameraService;

    private MutableLiveData<List<Place>> mPlaces;

    public CameraService getCameraService() {
        return cameraService;
    }

    public LiveData<List<Place>> getmPlaces() {
        return mPlaces;
    }

    public void init(Context context, Activity activity) {
        if (mPlaces != null) {
            return;
        }

        mPlacesRepository = PlacesRepository.getInstance();
        mPlaces = mPlacesRepository.getUsersDataSet();

        cameraService = new CameraService(context, activity);
    }

    public void addNewPlace(String name, LatLng latLng) {
        Place newPlace = new Place(name, latLng.latitude, latLng.longitude);

        List<Place> currentPlaces = mPlaces.getValue();
        if (currentPlaces != null) {
            currentPlaces.add(newPlace);
        }
        mPlaces.postValue(currentPlaces);
    }
}
