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
}
