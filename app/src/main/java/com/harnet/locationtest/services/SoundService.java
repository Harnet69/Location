package com.harnet.locationtest.services;

import android.content.Context;
import android.media.MediaPlayer;

import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Sound;
import com.harnet.locationtest.repositories.SoundRepository;

import java.util.List;
import java.util.stream.Collectors;

//TODO Make the class Asynch
public class SoundService {
    private SoundRepository soundRepo = SoundRepository.getInstance();
    private MediaPlayer mPlayer;


    public SoundService() {
        soundRepoInit();
    }

    private void soundRepoInit(){
        soundRepo.getSounds().add(new Sound("findingLocation", R.raw.sonar));
    }

    private List<Sound> findSound(final String soundName){
        return soundRepo.getSounds().stream()
                .filter(x -> x.getName().equals(soundName))
                .collect(Collectors.toList());
    }

    //TODO how to get sound from repository
    public void playSound(Context mContext, String sound){
        mPlayer = MediaPlayer.create(mContext, findSound(sound).get(0).getSource());
        mPlayer.start();
    }
}