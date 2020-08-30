package com.harnet.locationtest.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    // when Google map was downloaded and ready
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            // set initial user previous coordinates
            if (mLocationMapsActivityViewModel != null && mLocationMapsActivityViewModel.getmPersons().getValue().size() > 0) {
                // find and show user coordinates
                LatLng usersCoords = showUserPosition();
                // retrieve and show Places from Places List
                showPlaces(googleMap, usersCoords);
                // show place from QR "just Go"
                showPlaceFromQR();
            }

            // long click on a map
            //TODO Set dialog alert if user really want to save the place
            if (mMap != null) {
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                        try {
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Save \"" + getAddressFromCoord(latLng) + "\"?")
                                    .setMessage("Do you want to add the place to favorite?")

                                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                longClickOnMap(latLng);
                                                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            MarkerOptions options = new MarkerOptions().position(latLng);
//                                            mMap.addMarker(options);
                                        }
                                    })
                                    .show();
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

    // show user position on Google Map
    private LatLng showUserPosition() {
        LatLng userCoords = new LatLng(mLocationMapsActivityViewModel.getmPersons().getValue().get(0).getLat(), mLocationMapsActivityViewModel.getmPersons().getValue().get(0).getLng());
        if (userMarker == null) {
            MarkerOptions options = new MarkerOptions().position(userCoords)
                    .title("User")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
            userMarker = mMap.addMarker(options);
        }
        return userCoords;
    }

    // shows places from Places List on Google map
    private void showPlaces(GoogleMap googleMap, LatLng userCoords) {
        List<Place> lastPlaces = PlacesService.getInstance(getContext()).getFavouritePlaces();
        LatLng placeCoords;
        if (lastPlaces != null && lastPlaces.size() > 0) {
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
    }

    // get Place from Intent Extra (from QR scanner) and focus camera on it
    private void showPlaceFromQR() {
        String placeForFocus = getActivity().getIntent().getStringExtra("newPlaceLatLng");

        if (placeForFocus != null && !placeForFocus.equals("")) {
            Place focusPlace = null;
            try {
                focusPlace = (Place) PlacesService.getInstance(getContext()).getObjectSerializeService().deserialize(placeForFocus);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (focusPlace != null) {
                LatLng focusPlaceLatLng = new LatLng(focusPlace.getLat(), focusPlace.getLng());
                mMap.addMarker(new MarkerOptions().position(focusPlaceLatLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(focusPlaceLatLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(focusPlaceLatLng, 14));
                getActivity().getIntent().removeExtra("newPlaceLatLng");
            }
        }
    }

    // handle long click on a map
    @SuppressLint("ShowToast")
    private void longClickOnMap(LatLng latLng) throws IOException {
        //get address
        String placeAddress = getAddressFromCoord(latLng);

        if (placeAddress != null && !placeAddress.equals("")) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title(placeAddress);
            mMap.addMarker(options);

            PlacesService.getInstance(getContext()).addNewPlace(placeAddress, latLng);
            Log.i("Werere", "longClickOnMap: " + placeAddress + " : " + latLng);
        } else {
            Toast.makeText(getContext(), "Place's address doesn't exist!", Toast.LENGTH_SHORT).show();
        }
    }

    // get address from place coordinates
    private String getAddressFromCoord(LatLng latLng) throws IOException {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> placesFromCoords = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        String placeAddress = placesFromCoords.get(0).getThoroughfare();

        return placeAddress;
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