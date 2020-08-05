package com.harnet.locationtest.services;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import com.harnet.locationtest.R;
import com.harnet.locationtest.models.Sound;
import com.harnet.locationtest.repositories.SoundRepository;

import java.util.List;
import java.util.stream.Collectors;

//TODO Make the class Asynch
public class SoundService {
    private SoundRepository soundRepo = SoundRepository.getInstance();
    private SoundPool soundPool;
    private int[] soundsInPool;
    private Context context;


    public SoundService(Context contex) {
        this.context = contex;
        soundRepoInit();
        soundPoolInit();
    }

    private void soundRepoInit(){
        soundRepo.getSounds().add(new Sound("findingLocation", R.raw.sonar));
    }

    private void soundPoolInit(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        fillSoundPoolWithSounds();
    }

    private void fillSoundPoolWithSounds(){
        List<Sound> sounds = soundRepo.getSounds();
        Log.i("Soundd:", "fillSoundPoolWithSounds: " + sounds.toString());
        soundsInPool = new int[sounds.size()];
        for(int i = 0; i < soundRepo.getSounds().size(); i++){
            //TODO make a different variables for all a sound
            soundsInPool[i] = soundPool.load(context, soundRepo.getSounds().get(i).getSource(), 1);
        }
    }

    private List<Sound> findSound(final String soundName){
        return soundRepo.getSounds().stream()
                .filter(x -> x.getName().equals(soundName))
                .collect(Collectors.toList());
    }

    //TODO how to get sound from repository
    public void playSound(Context mContext, String sound){
        Log.i("Soundd:", "playSound: " + soundsInPool[0]);
        soundPool.play(soundsInPool[0], 1, 1, 0, 0, 1);

        MediaPlayer media = MediaPlayer.create(context, soundRepo.getSounds().get(0).getSource());
        media.start();
    }
}