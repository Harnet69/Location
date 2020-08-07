package com.harnet.locationtest.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.harnet.locationtest.R;

public class MainActivity extends AppCompatActivity implements LocationFragment.OnMessageSendListener {
    private Fragment fragment;
    private Bundle exchangeBundle; // bundle to keep data for exchanging

    private LocationFragment locationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new LocationFragment();

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
        switch (message){
            case "location" : startGameFragment();
                break;
        }
    }

    private void startGameFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_view_tag, new LocationFragment())
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
        locationFragment.getmLocationActivityViewModel().getSoundService().releaseSoundPool();
    }
}