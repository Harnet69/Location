package com.harnet.locationtest.services;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.harnet.locationtest.models.DeviceCamera;


public class CameraService {
    private Context context;
    private Activity activity;

    private DeviceCamera deviceCamera;
    private BarcodeDetector barcodeDetector;
    private Context appContext;

    private PermissionService permissionService;

    public CameraService(Context appContext, Activity activity) {
        this.appContext = appContext;
        this.activity = activity;
        deviceCamera = new DeviceCamera("cam1", appContext);
        // TODO can be error
        barcodeDetector = new BarcodeDetector.Builder(appContext)
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build();
        deviceCamera.setCameraSource(barcodeDetector);
        // check camera permission
        permissionService = new PermissionService(appContext, activity);
        permissionService.checkCameraPermissions();
    }

    public DeviceCamera getDeviceCamera() {
        return deviceCamera;
    }

    public BarcodeDetector getBarcodeDetector() {
        return barcodeDetector;
    }

    public PermissionService getPermissionService() {
        return permissionService;
    }
}
