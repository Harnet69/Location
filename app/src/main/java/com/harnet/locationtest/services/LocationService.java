package com.harnet.locationtest.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.harnet.locationtest.viewmodels.LocationMapsActivityViewModel;

import java.util.Locale;

public class LocationService {
    LocationMapsActivityViewModel mLocationMapsActivityViewModel;

    private Context context;
    private Activity activity;

    private PermissionService permissionService;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private String provider;
    private Geocoder geocoder;

    public LocationService(Context context, Activity activity, LocationMapsActivityViewModel mLocationMapsActivityViewModel) {
        this.context = context;
        this.activity = activity;
        this.mLocationMapsActivityViewModel = mLocationMapsActivityViewModel;

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
    private void initiateLocationTools() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        geocoder = new Geocoder(context, Locale.getDefault());

        locationListener = new LocationListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {
                if (mLocationMapsActivityViewModel != null) {
                    mLocationMapsActivityViewModel.changeUserCoords(location.getLatitude(), location.getLongitude(), location.getAltitude());
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
        permissionService = new PermissionService(context, activity);
        permissionService.checkLocationPermissions();

        if (locationManager != null && provider != null) {
            locationManager.requestLocationUpdates(provider, 10000, 0, locationListener);
        }
    }

    // finding last known location
    public Location getLastKnownLocation() {
        Location lastKnownLocation = null;
        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionService.checkLocationPermissions();
            }
            lastKnownLocation = locationManager.getLastKnownLocation(provider);
            }

        return lastKnownLocation;
    }
}
