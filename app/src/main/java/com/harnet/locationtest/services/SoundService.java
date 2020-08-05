package com.harnet.locationtest.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Sound;
import com.harnet.locationtest.repositories.SoundRepository;

import java.util.List;
import java.util.stream.Collectors;

//TODO Make the class Asynch
public class SoundService extends Service {
    private SoundRepository soundRepo = SoundRepository.getInstance();
    private MediaPlayer sonarSound;


    public SoundService() {
        soundRepoInit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void soundRepoInit(){
        soundRepo.getSounds().add(new Sound("findingLocation", R.raw.sonar));
    }

    private List<Sound> findSound(final String soundName){
        return soundRepo.getSounds().stream()
                .filter(x -> x.getName().equals(soundName))
                .collect(Collectors.toList());
    }

    //TODO can be cause of problems
    public void playSound(Context mContext, String sound){
        sonarSound = MediaPlayer.create(mContext, R.raw.sonar);
        sonarSound.start();
    }
}