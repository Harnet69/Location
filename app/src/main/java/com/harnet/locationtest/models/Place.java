package com.harnet.locationtest.models;

import java.io.Serializable;

public class Place implements Serializable {
    private Integer id = 0;
    private String name;
    private String description;
    private double lat;
    private double lng;
    private int image;

    //TODO make image parameter when get it by a camera/from gallery will be implemented
    public Place(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.description = "";
    }

    public Place(String name, String description, double lat, double lng, int image) {
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
