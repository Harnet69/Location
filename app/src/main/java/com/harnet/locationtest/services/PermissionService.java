package com.harnet.locationtest.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionService {
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Context context;
    private Activity activity;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;

    public PermissionService(Context context, Activity activity, LocationManager locationManager, LocationListener locationListener, String provider) {
        this.context = context;
        this.activity = activity;
        this.locationManager =locationManager;
        this.locationListener = locationListener;
        this.provider = provider;
    }

    public void checkPermissions(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }else {
            Log.i("TestLoc:", "Permission was granted already ");
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("TestLoc:", "Ask for permission: ");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(context, "Permission granted", Toast.LENGTH_LONG).show();

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    activity.finish();
                    //TODO how to send Intent?
//                    activity.startActivity();

                    Log.i("TestLoc:", "onRequestPermissionsResult: Refresh the page");
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
                    Log.i("TestLoc:", "onRequestPermissionsResult: Permission denied");
                    Toast.makeText(context, "User location unknown", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
