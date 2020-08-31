package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.AppSetting;

import java.util.ArrayList;
import java.util.List;

public class AppSettingsRepository {
    private List<AppSetting> appSettingsDataSet = new ArrayList<>();

    //TODO is necessary to do it as a singleton?
    public AppSettingsRepository() {
        appSettingsDataSet.add(new AppSetting("darkMode"));
        appSettingsDataSet.add(new AppSetting("muteSounds"));
        appSettingsDataSet.add(new AppSetting("SQLiteMode"));
    }

    public List<AppSetting> getAppSettings() {
        return appSettingsDataSet;
    }

    public MutableLiveData<List<AppSetting>> getAppSettingsSet() {
        MutableLiveData<List<AppSetting>> data = new MutableLiveData<>();
        data.setValue(appSettingsDataSet);
        return data;
    }
}
