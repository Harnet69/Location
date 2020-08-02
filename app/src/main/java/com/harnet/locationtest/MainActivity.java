package com.harnet.locationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.Provider;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView latTextView;
    private TextView lngTextView;
    private TextView placeTextView;
    private ImageView bgr_ImageView;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;
    private Geocoder geocoder;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView = findViewById(R.id.lat_textView);
        lngTextView = findViewById(R.id.lng_textView);
        placeTextView = findViewById(R.id.place_textView);
        bgr_ImageView = findViewById(R.id.bgr_imageView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
//
//        provider = locationManager.getBestProvider(new Criteria(), false);
        geocoder = new Geocoder(this, Locale.getDefault());

        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                Log.i("TestLoc:", "onLocationChanged: " + location);
//                latTextView.setText("Holla");
                List<Address> address = null;
                try {
                    address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                latTextView.setText("Longitude: " + location.getLatitude());
                lngTextView.setText("Latitude: " + location.getLongitude());
                placeTextView.setText("Place: " + address.get(0).getAddressLine(0).toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }else {
            Log.i("TestLoc:", "Permission was granted already ");
        }
        locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);
//        rotation background image
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000000);
        rotate.setInterpolator(new LinearInterpolator());

        ImageView image= (ImageView) bgr_ImageView;

        image.startAnimation(rotate);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("TestLoc:", "Ask for permission: ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();

                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    //TODO update user location(you can define time or distance for saving battery life)
                    // refresh activity after permission granted
                    finish();
                    startActivity(getIntent());

                    Log.i("TestLoc:", "onRequestPermissionsResult: Refresh the page");
                    // location-related task you need to do.
                    provider = locationManager.getBestProvider(new Criteria(), false);
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // location-related task you need to do.
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            //Request location updates:
                            if(provider != null){
                                locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);
                            }
                        }
                    }
                } else {
                    // permission denied, boo! Disable a functionality that depends on this permission.
//                    Intent mainActivityIntent = new Intent(this, MainActivity.class);
//                    startActivity(mainActivityIntent);
                    Log.i("TestLoc:", "onRequestPermissionsResult: Permission denied");
                    Toast.makeText(this, "User location unknown", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}