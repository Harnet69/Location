package com.harnet.locationtest.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Fragments;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.services.PlacesService;

import java.io.IOException;

public class PlaceEditorFragment extends Fragment {
    private EditText placeName;
    private EditText placeDescription;
    private Button discardBtn;
    private Button saveBtn;

    private Place placeForEdit;

    public PlaceEditorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeForEdit = getPlaceFromIntent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_editor, container, false);

        placeName = view.findViewById(R.id.place_name_PlainText);
        placeDescription = view.findViewById(R.id.place_description_PlainText);
        discardBtn = view.findViewById(R.id.discard_button);
        saveBtn = view.findViewById(R.id.save_button);

        // make keyboard hide by click outside these editViews
        hideKeyboardFromEditText(placeName, view);
        hideKeyboardFromEditText(placeDescription, view);

        // set listeners to btns
        discardChangesListener();
        saveChangesListener();

        // set place name in EditText field
        placeName.setText(placeForEdit.getName());
        placeDescription.setText(placeForEdit.getDescription());

        return view;
    }

    private Place getPlaceFromIntent() {
        String placeForEditSerialized = getActivity().getIntent().getStringExtra("editedPlaceLatLng");
        Place placeForEditDeSerialized = null;
        try {
            placeForEditDeSerialized = (Place) PlacesService.getInstance().getObjectSerializeService().deserialize(placeForEditSerialized);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return placeForEditDeSerialized;
    }

    // discard changes and redirect to QR&Favoutite fragment
    private void discardChangesListener() {
        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToQR();
            }
        });
    }

    // save changes and redirect to QR&Favourite fragment
    private void saveChangesListener() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlaceName = placeName.getText().toString();
                String newPlaceDescription = placeDescription.getText().toString();
                // check if something have been changed

                Log.i("PlacesChanges", "New name: " + newPlaceName + " old: " + placeForEdit.getName());
                Log.i("PlacesChanges", "New desc: " + newPlaceDescription + " old: " + placeForEdit.getDescription());
                if (!placeForEdit.getName().equals(newPlaceName) || !placeForEdit.getDescription().equals(newPlaceDescription)) {
                    // change place fields
                    placeForEdit.setName(newPlaceName);
                    placeForEdit.setDescription(newPlaceDescription);

                    // check if name isn't empty
                    if (!newPlaceName.equals("")) {

                        // try to save place
                        if (PlacesService.getInstance().editPlace(placeForEdit)) {
                            Toast.makeText(getContext(), "Changes were saved", Toast.LENGTH_SHORT).show();
                            redirectToQR();
                        } else {
                            Toast.makeText(getContext(), "Can't apply changes", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Name cant be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "You changed nothing!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void hideKeyboardFromEditText(EditText editText, View view) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
    }

    // make keyboard hide by click outside am editView
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // redirect to a QR&Favourite fragment
    public void redirectToQR() {
        Intent fragmentIntent = getActivity().getIntent();
        fragmentIntent.putExtra("fragmentIntent", Fragments.QR.toString());
        // put serialized Place object to Intent's extra
//        fragmentIntent.putExtra("newPlaceLatLng", PlacesService.getInstance().getObjectSerializeService().serialize(new Place("", newPlaceLatLng.latitude, newPlaceLatLng.longitude)));

        getActivity().finish();
        getActivity().startActivity(fragmentIntent);
    }
}