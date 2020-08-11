package com.harnet.locationtest.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.harnet.locationtest.R;
import com.harnet.locationtest.viewmodels.QRActivityViewModel;

import java.io.IOException;

public class QRFragment extends Fragment {
    private QRActivityViewModel qrActivityViewModel;
    private final int MY_CAMERA_REQUEST_CODE = 100;

    private SurfaceView surfaceView;
    private TextView textView;
    private Button goThereBtn;

    public QRFragment() {
        // Required empty public constructor
    }

    public QRActivityViewModel getQrActivityViewModel() {
        return qrActivityViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);

        surfaceView = (SurfaceView) view.findViewById(R.id.camera_preview_surfaceView);
        textView = (TextView) view.findViewById(R.id.camera_preview_TextView);
        goThereBtn = (Button) view.findViewById(R.id.go_there_button);

        //TODO change on MVVM atchitecture after testing
        qrActivityViewModel = new QRActivityViewModel();
        qrActivityViewModel.init(getContext(), getActivity());

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // ask user for camera permission
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    return;
                }

                try {
                    qrActivityViewModel.getCameraService().getDeviceCamera().getCameraSource().start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                qrActivityViewModel.getCameraService().getDeviceCamera().getCameraSource().stop();
            }
        });

        qrActivityViewModel.getCameraService().getBarcodeDetector().setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                // get a data after scanning of a QR code
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();

                if (qrCode.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            assert vibrator != null;
                            vibrator.vibrate(100);
                            // TODO do all work with received data here
                            textView.setText(qrCode.valueAt(0).displayValue);
                            goThereBtn.setVisibility(View.VISIBLE);
                            goThereBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO add place to list of recent places
//                                    Log.d(TAG, "onClick: add new Place " + PlacesController.getInstance().getRecentPlaces());
//                                    final Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
//                                    mapIntent.putExtra("placeFromQR", PlacesController.getInstance().createPlace(qrCode.valueAt(0).displayValue));
//                                    PlacesController.getInstance().addPlace(PlacesController.getInstance().createPlace(qrCode.valueAt(0).displayValue));
//                                    startActivity(mapIntent);
                                }
                            });
                        }
                    });
                }
            }
        });

        return view;
    }
}