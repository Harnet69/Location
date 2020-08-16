package com.harnet.locationtest.services;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.harnet.locationtest.models.DeviceCamera;
import com.harnet.locationtest.models.Fragments;

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

    //    private void prepareAndStartBarcodeDetector() {
//        mQrActivityViewModel.getCameraService().getBarcodeDetector().setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                // get a data after scanning of a QR code
//                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
//
//                if (qrCode.size() != 0) {
//                    textView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
//                            assert vibrator != null;
//                            vibrator.vibrate(100);
//                            // TODO do all work with received data here
//                            textView.setText(qrCode.valueAt(0).displayValue);
//                            goThereBtn.setVisibility(View.VISIBLE);
//                            goThereBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    // TODO add functionality to "GO THERE" button
//                                    String newPlaceCoord = qrCode.valueAt(0).displayValue;
//                                    double newPlaceLat = Double.parseDouble((newPlaceCoord.split(",")[0]));
//                                    double newPlaceLng = Double.parseDouble((newPlaceCoord.split(",")[1]));
//                                    Log.i("TestLoc:", "onClick to GO THERE button: " + qrCode.valueAt(0).displayValue);
//                                    mQrActivityViewModel.addNewPlace("NewPlace", new LatLng(newPlaceLat, newPlaceLng));
//
//                                    // redirect to maps fragment
//                                    Intent fragmentIntent = getActivity().getIntent();
//                                    fragmentIntent.putExtra("fragmentIntent", Fragments.MAPS.toString());
//                                    getActivity().finish();
//                                    getActivity().startActivity(fragmentIntent);
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        });
//    }
}
