package com.harnet.locationtest.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.harnet.locationtest.MapsFragment;
import com.harnet.locationtest.R;
import com.harnet.locationtest.repositories.UserCoordsRepository;

public class MainActivity extends AppCompatActivity implements LocationFragment.OnMessageSendListener {
    private Fragment fragment;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private Bundle exchangeBundle; // bundle to keep data for exchanging

    private LocationFragment locationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exchangeBundle = new Bundle();
        //display default fragment1
        if(savedInstanceState == null){
            startMainMenuFragment();
        }
    }

    // receive message from fragment1 and put it to exchange bundle
    @Override
    public void onMessageSend(String message) {
        exchangeBundle.putString("message", message);
        Log.i("Fragments", "onMessageSend: " + message);
        switch (message){
            case "location" : startLocationFragment();
                break;
            case "maps" : startMapsFragment();
                break;
            default: startMainMenuFragment();
        }
    }

    // start MainMenuFragment
    private void startMainMenuFragment(){
        fragment = new MainMenuFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentCont, fragment)
                .commit();
    }

    // start Location fragment with back stack (button) functionality
    private void startLocationFragment(){
        fragment = new LocationFragment();
        locationFragment = (LocationFragment) fragment; // for correct work of location
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentCont, fragment)
                .addToBackStack("location")
                .commit();
    }

    // start Maps fragment with back stack (button) functionality
    private void startMapsFragment(){
        fragment = new MapsFragment();
        locationFragment = (LocationFragment) fragment; // for correct work of location
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentCont, fragment)
                .addToBackStack("maps")
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(locationFragment != null){
            locationFragment.getmLocationActivityViewModel().getLocationService().getPermissionService().onRequestPermissionsResult(requestCode, permissions, grantResults, getIntent());
        }
    }
}