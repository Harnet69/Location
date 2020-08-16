package com.harnet.locationtest.services;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.harnet.locationtest.models.DeviceCamera;


public class CameraService {
    private DeviceCamera deviceCamera;
//    private BarcodeDetector barcodeDetector;

    private PermissionService permissionService;

    //TODO add argument QRActivityViewModel qrActivityViewModel
    public CameraService(Context appContext, Activity activity) {

        deviceCamera = new DeviceCamera("cam1", appContext);

        permissionService = new PermissionService(appContext, activity);
        permissionService.checkCameraPermissions();
    }

    public DeviceCamera getDeviceCamera() {
        return deviceCamera;
    }

    public PermissionService getPermissionService() {
        return permissionService;
    }
}