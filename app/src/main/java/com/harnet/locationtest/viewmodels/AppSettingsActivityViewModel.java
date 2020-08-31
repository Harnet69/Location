package com.harnet.locationtest.viewmodels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.AppSetting;
import com.harnet.locationtest.repositories.AppSettingsRepository;

import java.util.List;

public class AppSettingsActivityViewModel {
    private AppSettingsRepository appSettingsRepository;

    private MutableLiveData<List<AppSetting>> mAppSettings;

    public LiveData<List<AppSetting>> getmAppSettings() {
        return mAppSettings;
    }

    public void init() {
        if (mAppSettings != null) {
            return;
        }

        appSettingsRepository = new AppSettingsRepository();
        mAppSettings = appSettingsRepository.getAppSettingsSet();
    }

    //TODO here is methods for handling with settings repository

    //switch on SQLite mode
    public void switchSQLiteMode(String settingName, boolean turnOn) {
        List<AppSetting> updatedAppSettings = mAppSettings.getValue();

        if (updatedAppSettings != null && updatedAppSettings.size() > 0) {
            for (AppSetting appSetting : updatedAppSettings) {
                if (appSetting.getName().equals(settingName)) {
                    if (turnOn) {
                        appSetting.setOn();
                    } else {
                        appSetting.setOff();
                    }
                    mAppSettings.setValue(updatedAppSettings);
                }
            }
        }
    }
}
