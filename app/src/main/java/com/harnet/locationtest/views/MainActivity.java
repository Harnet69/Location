package com.harnet.locationtest.views;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Fragments;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.services.PlacesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements LocationFragment.OnMessageSendListener, MapsFragment.OnMessageSendListener {
    // create application menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    final String FRAGMENT_INTENT = "fragmentIntent";
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
        if (savedInstanceState == null) {
            try {
                // retrieve saved places from SharedPreferences
                if (PlacesService.getInstance().isPlacesInSharedPref(this)) {
                    PlacesService.getInstance().retrieveFromSharedPref(this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // used to recognize which fragment asks for permission
            //TODO produces a bug which don't allow to return with backstack after granted permission
            if (getIntent().getStringExtra(FRAGMENT_INTENT) == null || getIntent().getStringExtra(FRAGMENT_INTENT).equals(Fragments.MAIN)) {
//                startMainMenuFragment();
                startFragment(new MainMenuFragment(), Fragments.MAIN.toString());
            } else {
                switch (Objects.requireNonNull(getIntent().getStringExtra(FRAGMENT_INTENT))) {
                    case "location":
                        startFragment(new LocationFragment(), Fragments.LOCATION.toString());
                        break;
                    case "maps":
                        startFragment(new MapsFragment(), Fragments.MAPS.toString());
                        break;
                    case "qr":
                        startFragment(new QRFragment(), Fragments.QR.toString());
                        break;
                    case "main":
                        startFragment(new MainMenuFragment(), Fragments.MAIN.toString());
                        break;
                }
            }
        }
        listenBackStack();
    }

    // listen to a backstack
    private void listenBackStack() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                getIntent().removeExtra(FRAGMENT_INTENT);
                if (backCount == 0) {
                    startFragment(new MainMenuFragment(), "main");
                }
            }
        });
    }

    // receive message from fragments and starts an appropriate fragment
    @Override
    public void onMessageSend(String message) {
        exchangeBundle.putString("message", message);
        switch (message) {
            case "location":
                //TODO ENUM of fragment names
                startFragment(new LocationFragment(), "location");
                break;
            case "maps":
                startFragment(new MapsFragment(), "maps");
                break;
            case "qr":
                startFragment(new QRFragment(), "gr");
                break;
            default:
                startFragment(new MainMenuFragment(), "main");
        }
    }

    // common method for all fragments
    private void startFragment(Fragment newFragment, String fragmentName) {
        fragment = newFragment;

        switch (fragmentName) {
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

        Intent fragmentIntent = getIntent();
        LocationManager locationManager;
        LocationListener locationListener;
        String provider;

        //TODO make abstract class 'Fragment' for avoiding repetitive code
        if (locationFragment != null) {
            fragmentIntent.putExtra(FRAGMENT_INTENT, Fragments.LOCATION.toString());
            locationManager = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            locationListener = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            provider = locationFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            locationFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, fragmentIntent, locationManager, locationListener, provider);
        } else if (mapsFragment != null) {
            fragmentIntent.putExtra(FRAGMENT_INTENT, Fragments.MAPS.toString());
            locationManager = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationManager();
            locationListener = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getLocationListener();
            provider = mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getProvider();
            mapsFragment.getmLocationMapsActivityViewModel().getLocationService().getPermissionService().onRequestLocationPermissionsResult(requestCode, permissions, grantResults, fragmentIntent, locationManager, locationListener, provider);
        } else if (qrFragment != null) {
            fragmentIntent.putExtra(FRAGMENT_INTENT, Fragments.QR.toString());
            qrFragment.getmQrActivityViewModel().getCameraService().getPermissionService().onRequestCameraPermissionsResult(requestCode, permissions, grantResults, fragmentIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            PlacesService.getInstance().saveToSharedPref(this, PlacesService.getInstance().getmPlacesRepository().getPlacesDataSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}