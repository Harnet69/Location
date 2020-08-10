package com.harnet.locationtest.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.harnet.locationtest.R;

public class MainMenuFragment extends Fragment {
    private ImageButton locationBtn;
    LocationFragment.OnMessageSendListener onMessageSendListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    // interface for exchanging data between fragments
    public interface OnMessageSendListener{

        public void onMessageSend(String message);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main_menu, container, false);
        locationBtn = view.findViewById(R.id.location_imageButton);

        goLocation();

        return view;
    }

    private void goLocation(){
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessageSendListener.onMessageSend("location");
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        
        Activity activity = (Activity) context;
        try {
            onMessageSendListener = (LocationFragment.OnMessageSendListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "must implemented onMessageSend");
        }
    }
}