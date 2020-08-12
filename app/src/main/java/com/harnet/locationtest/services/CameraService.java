package com.harnet.locationtest.services;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.harnet.locationtest.models.DeviceCamera;
import com.harnet.locationtest.viewmodels.QRActivityViewModel;


public class CameraService {
    private Context context;
    private Activity activity;

    private QRActivityViewModel qrActivityViewModel;

    private DeviceCamera deviceCamera;
    private BarcodeDetector barcodeDetector;
    private Context appContext;

    private PermissionService permissionService;

    //TODO add argument QRActivityViewModel qrActivityViewModel
    public CameraService(Context appContext, Activity activity) {
        this.appContext = appContext;
        this.activity = activity;

        deviceCamera = new DeviceCamera("cam1", appContext);

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
