package com.harnet.locationtest.models;

import android.content.Context;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class DeviceCamera {
    private String name;
    private CameraSource cameraSource;
    private Context appContext;

    public DeviceCamera(String name, Context appContext) {
        this.name = name;
        this.appContext = appContext;
    }

    public CameraSource getCameraSource() {
        return cameraSource;
    }

    public void setCameraSource(BarcodeDetector barcodeDetector) {
        this.cameraSource = new CameraSource.Builder(appContext, barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();
    }
}
