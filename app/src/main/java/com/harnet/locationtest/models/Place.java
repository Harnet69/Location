package com.harnet.locationtest.models;

import java.io.Serializable;

public class Place implements Serializable {
    private String name;
    private double lat;
    private double lng;
    private int image;

    //TODO make image parameter when get it by a camera will be implemented
    public Place(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getImage() {
        return image;
    }
}
