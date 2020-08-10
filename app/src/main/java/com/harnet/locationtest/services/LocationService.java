package com.harnet.locationtest.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.harnet.locationtest.viewmodels.LocationActivityViewModel;
import java.util.Locale;

public class LocationService {
    LocationActivityViewModel mLocationActivityViewModel;

    private Context context;
    private Activity activity;

    private PermissionService permissionService;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;
    private Geocoder geocoder;

    public LocationService(Context context, Activity activity, LocationActivityViewModel mLocationActivityViewModel) {
        this.context = context;
        this.activity = activity;
        this.mLocationActivityViewModel = mLocationActivityViewModel;

        initiateLocationTools();
    }

    public PermissionService getPermissionService() {
        return permissionService;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public String getProvider() {
        return provider;
    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    // initiate needed location stuff
    @SuppressLint("MissingPermission")
    private void initiateLocationTools(){
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        geocoder = new Geocoder(context, Locale.getDefault());

        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                Log.i("TestLoc:", "onLocationChanged: " + location);

                if(mLocationActivityViewModel != null){
                    mLocationActivityViewModel.changeUserCoords(location.getLatitude(), location.getLongitude(), location.getAltitude());
                }
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
        permissionService = new PermissionService(context, activity, locationManager, locationListener, provider);
        permissionService.checkPermissions();

        if(locationManager != null && provider != null){
            locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);
        }
    }

    // finding last known location
    @SuppressLint("MissingPermission")
    public Location getLastKnownLocation() {
        Location lastKnownLocation = null;
            if(provider != null){
                lastKnownLocation = locationManager.getLastKnownLocation(provider);
                Log.i("TestLoc:", "Last known location: " + lastKnownLocation);
            }

        return lastKnownLocation;
    }
}
