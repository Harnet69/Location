package com.harnet.locationtest.services;

import android.content.Context;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.harnet.locationtest.models.DeviceCamera;

public class BarcodeService {
    private BarcodeDetector barcodeDetector;

    public BarcodeService(Context appContext, DeviceCamera deviceCamera) {
        this.barcodeDetector = new BarcodeDetector.Builder(appContext)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        deviceCamera.setCameraSource(barcodeDetector);
    }

    public BarcodeDetector getBarcodeDetector() {
        return barcodeDetector;
    }
}
