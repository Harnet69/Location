package com.harnet.locationtest.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.AppSetting;
import com.harnet.locationtest.models.Place;
import com.harnet.locationtest.repositories.AppSettingsRepository;
import com.harnet.locationtest.services.ObjectSerializeService;
import com.harnet.locationtest.services.SharedPreferencesAppSettingsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppSettingsActivityViewModel {
    private Context context;
    private AppSettingsRepository appSettingsRepository;

    private MutableLiveData<List<AppSetting>> mAppSettings;

    private ObjectSerializeService objectSerializeService = new ObjectSerializeService();
    private SharedPreferencesAppSettingsService sharedPreferencesAppSettingsService;

    public AppSettingsActivityViewModel(Context context) {
        this.context = context;
    }

    public LiveData<List<AppSetting>> getmAppSettings() {
        return mAppSettings;
    }

    public void init() {
        sharedPreferencesAppSettingsService = new SharedPreferencesAppSettingsService("appSettings", objectSerializeService);
        // retrieve appSettings in the first start of an app
        retrieveFromShredPreferences();
        if (mAppSettings != null) {
            return;
        }

        appSettingsRepository = AppSettingsRepository.getInstance();
        mAppSettings = appSettingsRepository.getAppSettingsSet();
    }

    //TODO try to do separate methods for each cases

    //switch on SQLite mode
    public void switchOnSQLiteMode(String settingName) {
        List<AppSetting> updatedAppSettings = mAppSettings.getValue();

        if (updatedAppSettings != null && updatedAppSettings.size() > 0) {
            for (AppSetting appSetting : updatedAppSettings) {
                if (appSetting.getName().equals(settingName)) {
                    appSetting.setOn();
                    mAppSettings.setValue(updatedAppSettings);
                }
            }
        }
    }

    //switch off SQLite mode
    public void switchOffSQLiteMode(String settingName) {
        List<AppSetting> updatedAppSettings = mAppSettings.getValue();

        if (updatedAppSettings != null && updatedAppSettings.size() > 0) {
            for (AppSetting appSetting : updatedAppSettings) {
                if (appSetting.getName().equals(settingName)) {
                    appSetting.setOff();
                    mAppSettings.setValue(updatedAppSettings);
                }
            }
        }
    }

    // save to shred preferences
    public void saveToShredPreferences(){
        sharedPreferencesAppSettingsService.saveToSharedPref(context, mAppSettings.getValue());
    }

    // add app setting to settings list
    public void retrieveFromShredPreferences(){
        List<AppSetting> retrievedAppSettings = null;
        try {
            retrievedAppSettings = sharedPreferencesAppSettingsService.getAppSettingsFromSharedPref(context);
            Log.i("MVVMinActionddd", "retrieveFromShredPreferences: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(retrievedAppSettings != null && retrievedAppSettings.size() > 0){
//            mAppSettings.postValue(retrievedAppSettings);
            AppSettingsRepository.getInstance().setAppSettingsDataSet(retrievedAppSettings);
        }
    }
}