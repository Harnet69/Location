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
//                startMainMenuFragment();
                startFragment(new MainMenuFragment(), "main");
            }else{
                switch (getIntent().getStringExtra("fragmentIntent")){
                    case "locationFrag" :
                        startFragment(new LocationFragment(), "location");
                        break;
                    case "mapsFrag" :
                        startFragment(new MapsFragment(), "maps");
                        break;
                    case "qrFrag" :
                        startFragment(new QRFragment(), "gr");
                    break;
                    default:
                        startFragment(new MainMenuFragment(), "main");
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
                getIntent().removeExtra("fragmentIntent");
                if (backCount == 0){
                    startFragment(new MainMenuFragment(), "main");
                }
            }
        });
    }

    // receive message from fragments and starts an appropriate fragment
    @Override
    public void onMessageSend(String message) {
        exchangeBundle.putString("message", message);
        Log.i("Fragments", "onMessageSend: " + message);
        switch (message){
            case "location" :
                //TODO ENUM of fragment names
                startFragment(new LocationFragment(), "location");
                break;
            case "maps" :
                startFragment(new MapsFragment(), "maps");
                break;
            case "qr" :
                startFragment(new QRFragment(), "gr");
                break;
            default:
                startFragment(new MainMenuFragment(), "main");
        }
    }

    // common method for all fragments
    private void startFragment(Fragment newFragment, String fragmentName){
        fragment = newFragment;

        switch (fragmentName){
            case "main":
                break;
            case "location":
                locationFragment = (LocationFragment) fragment; // for correct work of location
                break;
            case "maps":
                mapsFragment = (MapsFragment) fragment; // for correct work of location
                break;
            case "gr":
                qrFragment = (QRFragment) fragment; // for correct work of location
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentCont, fragment)
                .addToBackStack(fragmentName)
                .commit();
    }

    // have to be here, because of a bug of requestPermissions from fragments
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            final String FRAGMENT_INTENT = "fragmentIntent";

            Intent fragmentIntent = getIntent();
            LocationManager locationManager;
            LocationListener locationListener;
            String provider;

        //TODO make abstract class 'Fragment' for avoiding repetitive code
        Log.i("CameraPerm", "QR fragment: " + qrFragment);
        if(locationFragment != null){
            fragmentIntent.putExtra(FRAGMENT_INTENT, "locationFrag");
            locationManager = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            locationListener = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            provider = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            locationFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, fragmentIntent, locationManager, locationListener, provider);
        }
        else if(mapsFragment != null){
            fragmentIntent.putExtra(FRAGMENT_INTENT, "mapsFrag");
            locationManager = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            locationListener = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            provider = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, fragmentIntent, locationManager, locationListener, provider);
        }else if(qrFragment != null){
            fragmentIntent.putExtra(FRAGMENT_INTENT, "qrFrag");
            Log.i("CameraPerm", "onRequestCameraPermissionsResult: " + qrFragment);
            qrFragment.getmQrActivityViewModel().getCameraService().getPermissionService().onRequestCameraPermissionsResult(requestCode, permissions, grantResults, fragmentIntent);
        }
    }
}