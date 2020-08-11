package com.harnet.locationtest.models;

public class Place {
    private static int id;
    private String name;
    private double lat;
    private double lng;

    public Place(double lat, double lng) {
        id++;
        this.lat = lat;
        this.lng = lng;
    }

    public static int getId() {
        return id;
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
}
