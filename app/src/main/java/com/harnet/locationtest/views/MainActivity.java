package com.harnet.locationtest.views;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
            case "qr" : startQRFragment();
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
        if(locationFragment != null){
            LocationManager locationManager = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            LocationListener locationListener = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            String provider = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            locationFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, getIntent(), locationManager, locationListener, provider);
        }
        else if(mapsFragment != null){
            LocationManager locationManager = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            LocationListener locationListener = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            String provider = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, getIntent(), locationManager, locationListener, provider);
        }else if(qrFragment != null){
            Log.i("TestLoc:", "onRequestCameraPermissionsResult: ");
            //TODO make a correct chain to onRequestCameraPermissionsResult method
            qrFragment.getQrActivityViewModel().getCameraService().getPermissionService().onRequestCameraPermissionsResult(requestCode, permissions, grantResults, getIntent());
        }
    }
}