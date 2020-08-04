package com.harnet.locationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.viewmodels.MainActivityViewModel;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView latTextView;
    private TextView lngTextView;
    private TextView placeTextView;
    private ImageView bgr_ImageView;
    private ProgressBar progressBar;

    private MainActivityViewModel mMainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView = findViewById(R.id.lat_textView);
        lngTextView = findViewById(R.id.lng_textView);
        placeTextView = findViewById(R.id.place_textView);
        bgr_ImageView = findViewById(R.id.bgr_imageView);
        progressBar = findViewById(R.id.progress_bar);

        //MVVM observer
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init(this, this);
        mMainActivityViewModel.getmPersons().observe(this, new Observer<List<UserCoords>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<UserCoords> coords) {
                Log.i("TestLoc:", "Coordinates were changed" + coords.get(0).getLat() +":"+ coords.get(0).getLng());
                updateView(coords.get(0).getLat(), coords.get(0).getLng());
            }
        });

        // observe is coordinates were gotten in the first time
        mMainActivityViewModel.getmIsUpdated().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressBar.setVisibility(View.INVISIBLE);
                    bgr_ImageView.clearAnimation();
                }
            }
        });

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

        mMainActivityViewModel.getLocationService().getPermissionService().onRequestPermissionsResult(requestCode, permissions, grantResults, getIntent());
    }

    // update view by model coordinates
    private void updateView(double lat, double lng){
        List<Address> address = null;
        try {
            address = mMainActivityViewModel.getLocationService().getGeocoder().getFromLocation(lat, lng, 1);
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