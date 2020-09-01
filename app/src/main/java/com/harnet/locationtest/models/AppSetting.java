package com.harnet.locationtest.models;

import java.io.Serializable;

public class AppSetting implements Serializable {
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
