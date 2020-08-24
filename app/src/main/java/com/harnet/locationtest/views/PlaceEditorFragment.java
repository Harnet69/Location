package com.harnet.locationtest.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.services.PlacesService;

import java.io.IOException;

public class PlaceEditorFragment extends Fragment {

    public PlaceEditorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_place_editor, container, false);
        String placeForEditSerialized = getActivity().getIntent().getStringExtra("editedPlaceLatLng");
        Place placeForEditDeSerialized = null;
        try {
            placeForEditDeSerialized = (Place) PlacesService.getInstance().getObjectSerializeService().deserialize(placeForEditSerialized);
        } catch (IOException e) {
            e.printStackTrace();
        }
            Log.i("PlaceForEdit", "onCreateView: " + placeForEditDeSerialized.getName());


        return view;
    }
}