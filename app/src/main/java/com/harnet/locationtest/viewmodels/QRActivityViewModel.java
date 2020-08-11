package com.harnet.locationtest.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.harnet.locationtest.services.CameraService;

public class QRActivityViewModel extends ViewModel {
    private static final String TAG = "QRActivityViewModel";
    private CameraService cameraService;

    public CameraService getCameraService() {
        return cameraService;
    }

    public void init(Context context, Activity activity) {
        Log.i(TAG, "init: ");
        cameraService = new CameraService(context, activity);
    }
}
