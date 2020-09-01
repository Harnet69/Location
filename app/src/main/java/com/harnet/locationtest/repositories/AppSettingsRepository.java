package com.harnet.locationtest.repositories;

import androidx.lifecycle.MutableLiveData;

import com.harnet.locationtest.models.AppSetting;

import java.util.ArrayList;
import java.util.List;

public class AppSettingsRepository {
    private static AppSettingsRepository instance;
    private List<AppSetting> appSettingsDataSet = new ArrayList<>();

    //TODO is necessary to do it as a singleton?
    private AppSettingsRepository() {
        appSettingsDataSet.add(new AppSetting("darkMode"));
        appSettingsDataSet.add(new AppSetting("muteSounds"));
        appSettingsDataSet.add(new AppSetting("SQLiteMode"));
    }

    public static AppSettingsRepository getInstance() {
        if(instance == null){
            instance = new AppSettingsRepository();
        }
        return instance;
    }

    public List<AppSetting> getAppSettings() {
        return appSettingsDataSet;
    }

    public void setAppSettingsDataSet(List<AppSetting> appSettingsDataSet) {
        this.appSettingsDataSet = appSettingsDataSet;
    }

    public MutableLiveData<List<AppSetting>> getAppSettingsSet() {
        MutableLiveData<List<AppSetting>> data = new MutableLiveData<>();
        data.setValue(appSettingsDataSet);
        return data;
    }
}
