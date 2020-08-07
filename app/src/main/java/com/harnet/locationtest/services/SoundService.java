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
        soundRepo.getSounds().add(new Sound("muteOn", R.raw.click_on));
        soundRepo.getSounds().add(new Sound("muteOff", R.raw.click_off));
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

    //TODO how to get sound from repository
    public void playSound(String soundName){
        Log.i("Sound:", "playSound: " + soundsInPool[0]);
        switch (soundName){
            case "findingLocation":
                soundPool.play(soundsInPool[0], 0.3f, 0.3f, 0, 0, 1);
                break;

            case "muteOn":
                soundPool.play(soundsInPool[1], 0.7f, 0.7f, 0, 0, 1);
                break;

            case "muteOff":
                soundPool.play(soundsInPool[2], 0.7f, 0.7f, 0, 0, 1);
                break;
        }

//        MediaPlayer media = MediaPlayer.create(context, soundRepo.getSounds().get(0).getSource());
//        media.start();
    }

    public void releaseSoundPool(){
        soundPool.release();
        soundPool = null;
    }
}