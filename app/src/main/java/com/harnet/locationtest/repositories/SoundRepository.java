package com.harnet.locationtest.repositories;

import com.harnet.locationtest.models.Sound;

import java.util.ArrayList;
import java.util.List;

public class SoundRepository {
    private static SoundRepository instance;

    private List<Sound> sounds = new ArrayList<>();

    private SoundRepository() {
    }

    public static SoundRepository getInstance() {
        if(instance == null){
            instance = new SoundRepository();
        }
        return instance;
    }

    public List<Sound> getSounds() {
        return sounds;
    }
}
