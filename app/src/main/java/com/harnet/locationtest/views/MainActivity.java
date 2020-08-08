package com.harnet.locationtest.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.harnet.locationtest.R;

public class MainActivity extends AppCompatActivity implements LocationFragment.OnMessageSendListener {
    private Fragment fragment;
    private Bundle exchangeBundle; // bundle to keep data for exchanging

    private LocationFragment locationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new MainMenuFragment();

        exchangeBundle = new Bundle();
        //display default fragment1
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentCont, fragment)
                    .commit();
        }
    }

    // receive message from fragment1 and put it to exchange bundle
    @Override
    public void onMessageSend(String message) {
        exchangeBundle.putString("message", message);
        Log.i("Fragments", "onMessageSend: " + message);
        switch (message){
            case "location" : startLocationFragment();
                break;
        }
    }

    private void startLocationFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentCont, new LocationFragment())
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        locationFragment.getmLocationActivityViewModel().getLocationService().getPermissionService().onRequestPermissionsResult(requestCode, permissions, grantResults, getIntent());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationFragment != null){
            locationFragment.getmLocationActivityViewModel().getSoundService().releaseSoundPool();
        }
    }
}