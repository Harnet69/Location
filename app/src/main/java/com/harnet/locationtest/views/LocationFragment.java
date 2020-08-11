package com.harnet.locationtest.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.harnet.locationtest.R;
import com.harnet.locationtest.models.UserCoords;
import com.harnet.locationtest.viewmodels.LocationMapsActivityViewModel;

import java.io.IOException;
import java.util.List;

public class LocationFragment extends Fragment {
    private TextView latTextView;
    private TextView lngTextView;
    private TextView altTextView;
    private TextView placeTextView;
    private ImageView bgr_ImageView;
    private ProgressBar progressBar;
    private ImageView muteBtn;

    private LocationMapsActivityViewModel mLocationMapsActivityViewModel;

    private boolean isMuted;

    OnMessageSendListener onMessageSendListener;

    // interface for exchanging data between fragments
    public interface OnMessageSendListener{

        public void onMessageSend(String message);
    }

    public LocationFragment() {
    }

    public LocationMapsActivityViewModel getmLocationMapsActivityViewModel() {
        return mLocationMapsActivityViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_location, container, false);

        latTextView = view.findViewById(R.id.lat_textView);
        lngTextView = view.findViewById(R.id.lng_textView);
        altTextView = view.findViewById(R.id.alt_textView);
        placeTextView = view.findViewById(R.id.place_textView);
        bgr_ImageView = view.findViewById(R.id.bgr_imageView);
        progressBar = view.findViewById(R.id.progress_bar);
        muteBtn = view.findViewById(R.id.mute_btn_imageView);

        //MVVM observer
        mLocationMapsActivityViewModel = new ViewModelProvider(this).get(LocationMapsActivityViewModel.class);
        mLocationMapsActivityViewModel.init(getContext(), getActivity());
        mLocationMapsActivityViewModel.getmPersons().observe(getActivity(), new Observer<List<UserCoords>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(List<UserCoords> coords) {
                if (coords != null && coords.size() > 0) {
                    Log.i("TestLoc:", "Coordinates were changed" + coords.get(0).getLat() + ":" + coords.get(0).getLng());
                    updateView(coords.get(0).getLat(), coords.get(0).getLng(), coords.get(0).getAlt());
                }
            }
        });


        // observe is coordinates were gotten in the first time
        mLocationMapsActivityViewModel.getmIsUpdated().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isChanged) {
                if (isChanged) {
                    progressBar.setVisibility(View.INVISIBLE);
                    bgr_ImageView.clearAnimation();
                    //
                    if (!isMuted) {
                        mLocationMapsActivityViewModel.getSoundService().playSound("findingLocation");
                    }
                }
            }
        });

//        rotation background image
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(100000);
                rotate.setInterpolator(new LinearInterpolator());

                ImageView image = (ImageView) bgr_ImageView;

                image.startAnimation(rotate);
            }
        });
        muteSound();

        return view;
    }

    private void updateView(double lat, double lng, double alt){
        List<Address> address = null;
        try {
            address = mLocationMapsActivityViewModel.getLocationService().getGeocoder().getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        latTextView.setText(String.format("%s%s", getString(R.string.Lat), lat));
        lngTextView.setText(String.format("%s%s", getString(R.string.Long), lng));
        altTextView.setText(String.format("%s%s", getString(R.string.Alt), alt));
        if(address != null && address.size() > 0) {
            placeTextView.setText(String.format("%s%s", getString(R.string.place), address.get(0).getAddressLine(0)));
        }
    }

    public void muteSound(){
        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMuted){
                    muteBtn.setImageResource(R.drawable.unmute_btn);
                    isMuted = true;
                    mLocationMapsActivityViewModel.getSoundService().playSound("muteOn");
                }else{
                    muteBtn.setImageResource(R.drawable.mute_btn);
                    isMuted = false;
                    mLocationMapsActivityViewModel.getSoundService().playSound("muteOff");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onMessageSendListener = (OnMessageSendListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "must implemented onMessageSend");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationMapsActivityViewModel.getLocationService().getLocationManager().removeUpdates(mLocationMapsActivityViewModel.getLocationService().getLocationListener());
        mLocationMapsActivityViewModel.getSoundService().releaseSoundPool();
    }
}