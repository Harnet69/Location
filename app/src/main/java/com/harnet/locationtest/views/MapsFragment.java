package com.harnet.locationtest.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.harnet.locationtest.R;
import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.viewmodels.LocationActivityViewModel;

import java.util.List;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    private Marker userMarker;

    private LocationActivityViewModel mLocationActivityViewModel;
    OnMessageSendListener onMessageSendListener;

    // interface for exchanging data between fragments
    public interface OnMessageSendListener{
        public void onMessageSend(String message);
    }

    public MapsFragment(){
    }

    public LocationActivityViewModel getmLocationActivityViewModel() {
        return mLocationActivityViewModel;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            // set initial user previous coordinates
            if(mLocationActivityViewModel != null && mLocationActivityViewModel.getmPersons().getValue().size() > 0){
                LatLng userCoords = new LatLng(mLocationActivityViewModel.getmPersons().getValue().get(0).getLat(), mLocationActivityViewModel.getmPersons().getValue().get(0).getLng());
                userMarker = googleMap.addMarker(new MarkerOptions().position(userCoords).title("User"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userCoords));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userCoords));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoords, 12));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //MVVM observer
        mLocationActivityViewModel = new ViewModelProvider(this).get(LocationActivityViewModel.class);
        mLocationActivityViewModel.init(getContext(), getActivity());
        mLocationActivityViewModel.getmPersons().observe(getActivity(), new Observer<List<UserCoords>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<UserCoords> coords) {
                if (coords != null && coords.size() > 0) {
                    Log.i("TestLoc:", "Coordinates on a map were changed" + coords.get(0).getLat() + ":" + coords.get(0).getLng());
                        //update user position on a map
                    if(userMarker != null && mMap != null){
                        LatLng userCoords = new LatLng(coords.get(0).getLat(), coords.get(0).getLng());
                        userMarker.setPosition(userCoords);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userCoords));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoords, 12));
                    }
                }
            }
        });

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onMessageSendListener = (OnMessageSendListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "must implemented onMessageSend");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationActivityViewModel.getLocationService().getLocationManager().removeUpdates(mLocationActivityViewModel.getLocationService().getLocationListener());
    }
}