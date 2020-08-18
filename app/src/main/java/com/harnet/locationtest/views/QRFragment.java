package com.harnet.locationtest.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Fragments;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.services.PlacesService;
import com.harnet.locationtest.viewmodels.QRActivityViewModel;

import java.io.IOException;
import java.util.List;

public class QRFragment extends Fragment {
    private String name = "qr";

    private QRActivityViewModel mQrActivityViewModel;

    private SurfaceView surfaceView;
    private TextView textView;
    private Button goThereBtn;
    private Button saveAndGoBtn;
    private EditText placeNameEditText;
    private RecyclerView favoritePlacesRecyclerView;

    public QRFragment() {
    }

    public String getName() {
        return name;
    }

    public QRActivityViewModel getmQrActivityViewModel() {
        return mQrActivityViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r, container, false);

        surfaceView = (SurfaceView) view.findViewById(R.id.camera_preview_surfaceView);
        textView = (TextView) view.findViewById(R.id.camera_preview_TextView);
        goThereBtn = (Button) view.findViewById(R.id.go_there_button);
        saveAndGoBtn = (Button) view.findViewById(R.id.save_go_button);
        placeNameEditText = (EditText) view.findViewById(R.id.editTextTextPlaceName);
        favoritePlacesRecyclerView = (RecyclerView) view.findViewById(R.id.favorite_places_recyclerView);

        mQrActivityViewModel = new QRActivityViewModel();
        mQrActivityViewModel.init(getContext(), getActivity());
        mQrActivityViewModel.getmPlaces().observe(getActivity(), new Observer<List<Place>>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<Place> places) {
                if (places != null && places.size() > 0) {
                    Log.i("TestLoc:", "Place was added" + places.get(places.size() - 1).getLat());
                    //TODO do something after adding new place
                }
            }
        });
        // if camera permission was granted - start camera
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            prepareAndStartCamera();
            prepareAndStartBarcodeDetector();
        }

        return view;
    }

    //preparing and starting camera view
    private void prepareAndStartCamera() {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    // turn on a camera
                    mQrActivityViewModel.getCameraService().getDeviceCamera().getCameraSource().start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mQrActivityViewModel.getCameraService().getDeviceCamera().getCameraSource().stop();
            }
        });
    }

    private void prepareAndStartBarcodeDetector() {
        mQrActivityViewModel.getBarcodeService().getBarcodeDetector().setProcessor(new Detector.Processor<Barcode>() {
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
                            saveAndGoBtn.setVisibility(View.VISIBLE);
                            placeNameEditText.setVisibility(View.VISIBLE);
                            favoritePlacesRecyclerView.setVisibility(View.INVISIBLE);

                            // get and parse plase coordinates
                            String newPlaceCoord = qrCode.valueAt(0).displayValue;
                            double newPlaceLat = Double.parseDouble((newPlaceCoord.split(",")[0]));
                            double newPlaceLng = Double.parseDouble((newPlaceCoord.split(",")[1]));

                            //JUST GO button
                            goThereBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //redirect to a point from QR code, not to the last added
                                    PlacesService.getInstance().addNewPlace("", new LatLng(newPlaceLat, newPlaceLng));
                                    redirectToMaps();
//
                                }
                            });

                            //Save $ Go button
                            saveAndGoBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // check if place name is empty and if place exists in places
                                    if (!placeNameEditText.getText().toString().equals("")) {
                                        if (PlacesService.getInstance().addNewPlace(placeNameEditText.getText().toString(), new LatLng(newPlaceLat, newPlaceLng))) {
                                            // redirect to maps fragment
                                            redirectToMaps();
                                        } else {
                                            Toast.makeText(getContext(), "Place exists!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Name is empty!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }
            }
        });
    }

    // redirect to a maps fragment
    private void redirectToMaps() {
        Intent fragmentIntent = getActivity().getIntent();
        fragmentIntent.putExtra("fragmentIntent", Fragments.MAPS.toString());
        getActivity().finish();
        getActivity().startActivity(fragmentIntent);
    }
}