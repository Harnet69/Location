package com.harnet.locationtest.models;

public enum Fragments {
    MAIN("main"),
    LOCATION("location"),
    MAPS("maps"),
    QR("qr"),
    PROFILE("profile"),
    SETTINGS("settings"),
    HELP("help");

    private String name;

    Fragments(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
