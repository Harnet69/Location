package com.harnet.locationtest.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.harnet.locationtest.models.AppSetting;
import com.harnet.locationtest.models.Place;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedPreferencesAppSettingsService {
    //TODO think about do this method universal
    private final String SHARED_PREFERENCES_NAME = "com.harnet.sharedpreferences";
    private final String SHARED_PREFERENCES_SAVE_NAME;

    private ObjectSerializeService objectSerializeService;

    public SharedPreferencesAppSettingsService(String nameInSharedPref, ObjectSerializeService objectSerializeService) {
        this.objectSerializeService = objectSerializeService;
        SHARED_PREFERENCES_SAVE_NAME = nameInSharedPref;
    }

    // check is appSetting in SharedPreferences
    public boolean isAppSettingInSharedPref(Context context) throws IOException {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(SHARED_PREFERENCES_SAVE_NAME, objectSerializeService.serialize(new ArrayList<String>())) != null;
    }

    // save appSettings to SharedPreferences
    //TODO make a second argument as generic to make class universal
    public void saveToSharedPref(Context context, List<AppSetting> appSettings){
        try {
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(SHARED_PREFERENCES_SAVE_NAME, objectSerializeService.serialize((Serializable) appSettings))
                    .apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // retrieve appSettings from SharedPreferences and fill appSettings List
    public List<AppSetting> getAppSettingsFromSharedPref(Context context) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Object retrievedPlacesObject = objectSerializeService.deserialize(sharedPreferences.getString(SHARED_PREFERENCES_SAVE_NAME, objectSerializeService.serialize(new ArrayList<>())));
        List<AppSetting> retrievedAppSetings = null;
        try {
            retrievedAppSetings = Collections.unmodifiableList((List<AppSetting>) retrievedPlacesObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // record all favourite places to SHaredPreferences
        return  retrievedAppSetings;
    }

//    // save app settings to SharedPreferences
//    public void retrieveFromSharedPref(Context context) throws IOException {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        Object retrievedPlacesObject = objectSerializeService.deserialize(sharedPreferences.getString(SHARED_PREFERENCES_SAVE_NAME, objectSerializeService.serialize(new ArrayList<Place>())));
//        List<Place> retrievedPlaces = null;
//        try {
//            retrievedPlaces = Collections.unmodifiableList((List<Place>) retrievedPlacesObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // record all favourite places to SHaredPreferences
//        if (retrievedPlaces != null && retrievedPlaces.size() > 0) {
//            for (Place place : retrievedPlaces) {
//                PlacesService.getInstance(context).addNewPlace(place);
//            }
//        }
//    }
}