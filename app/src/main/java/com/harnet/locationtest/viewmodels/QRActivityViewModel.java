package com.harnet.locationtest.viewmodels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.repositories.PlacesRepository;
import com.harnet.locationtest.services.BarcodeService;
import com.harnet.locationtest.services.CameraService;
import com.harnet.locationtest.services.PlacesService;

import java.util.List;

public class QRActivityViewModel extends ViewModel {
    private PlacesRepository mPlacesRepository;

    private CameraService cameraService;
    private BarcodeService barcodeService;

    private MutableLiveData<List<Place>> mPlaces;

    public CameraService getCameraService() {
        return cameraService;
    }

    public BarcodeService getBarcodeService() {
        return barcodeService;
    }

    public LiveData<List<Place>> getmPlaces() {
        return mPlaces;
    }

    public void init(Context context, Activity activity) {
        if (mPlaces != null) {
            return;
        }

        mPlaces = PlacesService.getInstance(context).getmPlacesRepository();

        cameraService = new CameraService(context, activity);

        barcodeService = new BarcodeService(context, cameraService.getDeviceCamera());
    }

    // add new place to places
    public boolean addNewPlace(String name, LatLng latLng) {
        Place newPlace = new Place(name, latLng.latitude, latLng.longitude);
        List<Place> currentPlaces = mPlaces.getValue();

        if (currentPlaces != null && !isPlaceInPlaces(currentPlaces, latLng)) {
            currentPlaces.add(newPlace);
            mPlaces.postValue(currentPlaces);
            return true;
        }
        return false;
    }

    //TODO where it should be???
    public void deleteUnnamedPlaces() {
        List<Place> currentPlaces = mPlaces.getValue();

        if (currentPlaces != null) {
            for (Place place : currentPlaces) {
                if (place.getName().equals("")) {
                    currentPlaces.remove(place);
                }
            }
        }
    }

    // check if the place in places repository already
    private boolean isPlaceInPlaces(List<Place> places, LatLng placeCoords) {
        for (Place place : places) {
            if (placeCoords.latitude == place.getLat() && placeCoords.longitude == place.getLng()) {
                return true;
            }
        }
        return false;
    }
}
