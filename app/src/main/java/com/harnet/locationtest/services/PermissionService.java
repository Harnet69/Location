package com.harnet.locationtest.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionService {
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final int MY_CAMERA_REQUEST_CODE = 98;

    private Context context;
    private Activity activity;

    public PermissionService(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    // check location permission
    public void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    // check camera permission
    public void checkCameraPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void onRequestLocationPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, Intent intent, LocationManager locationManager, LocationListener locationListener, String provider) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission granted", Toast.LENGTH_LONG).show();
                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    // location-related task you need to do.
                    provider = locationManager.getBestProvider(new Criteria(), false);
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            //Request location updates:
                            if (provider != null) {
                                locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);
                            }
                        }
                    }
                } else {
                    // permission denied, boo! Disable a functionality that depends on this permission
                    Toast.makeText(context, "User location unknown", Toast.LENGTH_LONG).show();
                    intent.removeExtra("fragmentIntent");
                }

                pauseBeforeRedirect(intent);
            }
        }
    }

    public void onRequestCameraPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, Intent intent) {
        Log.i("CameraPerm", "request code: " + requestCode);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            Log.i("CameraPerm", "onRequestCameraPermissionsResult: ");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
                intent.removeExtra("fragmentIntent");
            }
            pauseBeforeRedirect(intent);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void pauseBeforeRedirect(Intent intent) {
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                activity.finish();
                activity.startActivity(intent);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}