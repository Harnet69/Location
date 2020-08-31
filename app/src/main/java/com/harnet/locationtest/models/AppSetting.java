package com.harnet.locationtest.models;

public class AppSetting {
    private String name;
    private boolean isOn;

    public AppSetting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public void setOn() {
        isOn = true;
    }

    public void setOff() {
        isOn = false;
    }
}
