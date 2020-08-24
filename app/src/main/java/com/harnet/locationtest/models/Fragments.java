package com.harnet.locationtest.models;

public enum Fragments {
    MAIN("main"),
    LOCATION("location"),
    MAPS("maps"),
    QR("qr"),
    PROFILE("profile"),
    SETTINGS("settings"),
    HELP("help"),
    PLACE_EDITOR("place_editor");

    private String name;

    Fragments(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
