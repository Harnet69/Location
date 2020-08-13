package com.harnet.locationtest.views;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.harnet.locationtest.R;

public class MainActivity extends AppCompatActivity implements LocationFragment.OnMessageSendListener, MapsFragment.OnMessageSendListener {
    private Fragment fragment;
    private Bundle exchangeBundle; // bundle to keep data for exchanging

    private LocationFragment locationFragment;
    private MapsFragment mapsFragment;
    private QRFragment qrFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exchangeBundle = new Bundle();
        //display default menu
        if(savedInstanceState == null){

            // used to recognize which fragment asks for permission
            //TODO produces a bug which don't allow to return with backstack after granted permission
            if(getIntent().getStringExtra("fragmentIntent") == null || getIntent().getStringExtra("fragmentIntent").equals("main")){
                startMainMenuFragment();
            }else{
                switch (getIntent().getStringExtra("fragmentIntent")){
                    case "locationFrag" :
                        startLocationFragment();
                        break;
                    case "mapsFrag" :
                        startMapsFragment();
                        break;
                    case "qrFrag" :
                        startQRFragment();
                        break;
                }
            }
        }
        listenBackStack();
    }


    // listen to a backstack
    private void listenBackStack(){
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.i("Backstask", "onBackStackChanged: "  + backCount);
                if (backCount == 0){
                    startMainMenuFragment();
                }
            }
        });
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
            case "qr" : startQRFragment();
                break;
            default: startMainMenuFragment();
        }
    }

    //TODO think about generic Fragment to avoid repetitive code

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
        mapsFragment = (MapsFragment) fragment; // for correct work of location
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentCont, fragment)
                .addToBackStack("maps")
                .commit();
    }

    // start QR fragment with back stack (button) functionality
    private void startQRFragment(){
        fragment = new QRFragment();
        qrFragment = (QRFragment) fragment; // for correct work of location
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentCont, fragment)
                .addToBackStack("qr")
                .commit();
    }

    // have to be here, because of a bug of requestPermissions from fragments
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO make generic 'Fragment' class for avoiding repetitive code
        Log.i("CameraPerm", "QR fragment: " + qrFragment);
        if(locationFragment != null){
            Intent fragmentIntent = getIntent();
            fragmentIntent.putExtra("fragmentIntent", "locationFrag");
            LocationManager locationManager = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            LocationListener locationListener = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            String provider = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            locationFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, fragmentIntent, locationManager, locationListener, provider);
        }
        else if(mapsFragment != null){
            Intent fragmentIntent = getIntent();
            fragmentIntent.putExtra("fragmentIntent", "mapsFrag");
            LocationManager locationManager = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            LocationListener locationListener = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            String provider = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, fragmentIntent, locationManager, locationListener, provider);
        }else if(qrFragment != null){
            Intent fragmentIntent = getIntent();
            fragmentIntent.putExtra("fragmentIntent", "qrFrag");
            Log.i("CameraPerm", "onRequestCameraPermissionsResult: " + qrFragment);
            qrFragment.getmQrActivityViewModel().getCameraService().getPermissionService().onRequestCameraPermissionsResult(requestCode, permissions, grantResults, fragmentIntent);
        }
    }
}