package com.harnet.locationtest.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.repositories.PlacesRepository;
import com.harnet.locationtest.services.PlacesService;
import com.harnet.locationtest.viewmodels.LocationMapsActivityViewModel;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements GoogleMap.OnMapLongClickListener {
    private String name = "maps";

    private GoogleMap mMap;
    private Marker userMarker;
    private Marker placeMarker;

    private LocationMapsActivityViewModel mLocationMapsActivityViewModel;
    private OnMessageSendListener onMessageSendListener;

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    // interface for exchanging data between fragments
    public interface OnMessageSendListener {
        public void onMessageSend(String message);
    }

    public MapsFragment() {
    }

    public String getName() {
        return name;
    }

    public LocationMapsActivityViewModel getmLocationMapsActivityViewModel() {
        return mLocationMapsActivityViewModel;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            // set initial user previous coordinates
            if (mLocationMapsActivityViewModel != null && mLocationMapsActivityViewModel.getmPersons().getValue().size() > 0) {

                // shows user position
                LatLng userCoords = new LatLng(mLocationMapsActivityViewModel.getmPersons().getValue().get(0).getLat(), mLocationMapsActivityViewModel.getmPersons().getValue().get(0).getLng());
                if (userMarker == null) {
                    MarkerOptions options = new MarkerOptions().position(userCoords)
                            .title("User")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
                    userMarker = mMap.addMarker(options);
                }

                // shows places from places list on Google map
                //TODO can be a cause of some bug
                List<Place> lastPlaces = PlacesService.getInstance().getmPlacesRepository().getPlacesDataSet();
                LatLng placeCoords;
                if (lastPlaces != null && lastPlaces.size() > 0) {
//                    Log.i("Places", "onMapReady: " + lastPlaces);
                    LatLng lastAddedPlace = null;
                    for (Place place : lastPlaces) {
                        placeCoords = new LatLng(place.getLat(), place.getLng());
                        placeMarker = googleMap.addMarker(new MarkerOptions().position(placeCoords).title(place.getName()));
                        lastAddedPlace = placeCoords;
                    }
                    // focus camera on the last added place
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lastAddedPlace));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastAddedPlace, 12));
                } else {
                    // if any place in favorite places - focus to user position
                    if (userMarker != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userCoords));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoords, 12));
                    }
                }

                // focus on place from QR scanner
                String placeForFocus = getActivity().getIntent().getStringExtra("newPlaceLatLng");

                if (placeForFocus != null && !placeForFocus.equals("")) {
                    Place focusPlace = null;
                    try {
                        focusPlace = (Place) PlacesService.getInstance().getObjectSerializeService().deserialize(placeForFocus);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(focusPlace != null){
                        LatLng focusPlaceLatLng = new LatLng(focusPlace.getLat(), focusPlace.getLng());
                        mMap.addMarker(new MarkerOptions().position(focusPlaceLatLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(focusPlaceLatLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(focusPlaceLatLng, 14));
                        getActivity().getIntent().removeExtra("newPlaceLatLng");
                    }
                }
            }
            // long click on a map
            if (mMap != null) {
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        try {
                            longClickOnMap(latLng);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //MVVM observer
        mLocationMapsActivityViewModel = new ViewModelProvider(this).get(LocationMapsActivityViewModel.class);
        mLocationMapsActivityViewModel.init(getContext(), getActivity());
        mLocationMapsActivityViewModel.getmPersons().observe(getActivity(), new Observer<List<UserCoords>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<UserCoords> coords) {
                if (coords != null && coords.size() > 0) {
                    //update user position on a map
                    if (userMarker != null && mMap != null) {
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

    // handle long click on a map
    //TODO implement new place saving
    @SuppressLint("ShowToast")
    private void longClickOnMap(LatLng latLng) throws IOException {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> placesFromCoords = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        String placeAddress = placesFromCoords.get(0).getThoroughfare();

        if (placeAddress != null && !placeAddress.equals("")) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title(placesFromCoords.get(0).getThoroughfare());
            mMap.addMarker(options);

            PlacesService.getInstance().addNewPlace(placesFromCoords.get(0).getThoroughfare(), latLng);
        } else {
            Toast.makeText(getContext(), "Place's address doesn't exist!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onMessageSendListener = (OnMessageSendListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implemented onMessageSend");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // stop asking for user location after fragment destroying

        mLocationMapsActivityViewModel.getLocationService().getLocationManager().removeUpdates(mLocationMapsActivityViewModel.getLocationService().getLocationListener());
    }
}