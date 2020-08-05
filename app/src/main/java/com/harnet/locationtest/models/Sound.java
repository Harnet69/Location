package com.harnet.locationtest.models;

public class Sound {
    private String name;
    private int source;

    public Sound(String name, int source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public int getSource() {
        return source;
    }
}
