package com.harnet.locationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.services.LocationService;
import com.harnet.locationtest.viewmodels.MainActivityViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView latTextView;
    private TextView lngTextView;
    private TextView placeTextView;
    private ImageView bgr_ImageView;

    private MainActivityViewModel mMainActivityViewModel;

    private LocationService locationService;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;
    private Geocoder geocoder;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView = findViewById(R.id.lat_textView);
        lngTextView = findViewById(R.id.lng_textView);
        placeTextView = findViewById(R.id.place_textView);
        bgr_ImageView = findViewById(R.id.bgr_imageView);

        //TODO MVVM
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mMainActivityViewModel.init();

        mMainActivityViewModel.getmPersons().observe(this, new Observer<List<UserCoords>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<UserCoords> coords) {
                Log.i("TestLoc:", "Coordinates were changed" + coords.get(0).getLat() +":"+ coords.get(0).getLng());
                updateView(coords.get(0).getLat(), coords.get(0).getLng());
            }
        });
        // location service starts automatically
        locationService = new LocationService(this, MainActivity.this, mMainActivityViewModel);

//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        provider = locationManager.getBestProvider(new Criteria(), false);
//
//        provider = locationManager.getBestProvider(new Criteria(), false);
//        geocoder = new Geocoder(this, Locale.getDefault());

//        locationListener = new LocationListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.i("TestLoc:", "onLocationChanged: " + location);
//                //TODO MVVM
//                mMainActivityViewModel.changeUserCoords(location.getLatitude(), location.getLongitude());
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//        checkPermissions();

//        locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);

//        rotation background image
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(100000);
                rotate.setInterpolator(new LinearInterpolator());

                ImageView image= (ImageView) bgr_ImageView;

                image.startAnimation(rotate);
            }
        });
    }

//    private void checkPermissions(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_LOCATION);
//        }else {
//            Log.i("TestLoc:", "Permission was granted already ");
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationService.getPermissionService().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.i("TestLoc:", "Ask for permission: ");
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
//
//                    // permission was granted, yay!
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED
//                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    //TODO update user location(you can define time or distance for saving battery life)
//                    // refresh activity after permission granted
//                    finish();
//                    startActivity(getIntent());
//
//                    Log.i("TestLoc:", "onRequestPermissionsResult: Refresh the page");
//                    // location-related task you need to do.
//                    provider = locationManager.getBestProvider(new Criteria(), false);
//                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        // location-related task you need to do.
//                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                            //Request location updates:
//                            if(provider != null){
//                                locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);
//                            }
//                        }
//                    }
//                } else {
//                    // permission denied, boo! Disable a functionality that depends on this permission
//                    Log.i("TestLoc:", "onRequestPermissionsResult: Permission denied");
//                    Toast.makeText(this, "User location unknown", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

    // update view by model coordinates
    private void updateView(double lat, double lng){
        List<Address> address = null;
        try {
            address = locationService.getGeocoder().getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latTextView.setText("Longitude: " + lat);
        lngTextView.setText("Latitude: " + lng);
        if(address.size()>0){
            placeTextView.setText("Place: " + address.get(0).getAddressLine(0).toString());
        }
    }
}