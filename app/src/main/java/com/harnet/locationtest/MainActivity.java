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

        //TODO can be a fail
        Location location = locationService.getLocationManager().getLastKnownLocation(locationService.getProvider());
        Log.i("TestLoc:", "Last known location: " + location);
        if(location != null){
            mMainActivityViewModel.changeUserCoords(location.getLatitude(), location.getLongitude());
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        locationService.getPermissionService().onRequestPermissionsResult(requestCode, permissions, grantResults, getIntent());
    }

    // update view by model coordinates
    private void updateView(double lat, double lng){
        List<Address> address = null;
        try {
            address = locationService.getGeocoder().getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latTextView.setText(String.format("%s%s", getString(R.string.Lat), lat));
        lngTextView.setText(String.format("%s%s", getString(R.string.Long), lng));
        assert address != null;
        if(address.size()>0) {
            placeTextView.setText(String.format("%s%s", getString(R.string.place), address.get(0).getAddressLine(0)));
        }
    }
}