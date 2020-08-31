package com.harnet.locationtest.views.systemMenu;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.harnet.locationtest.R;
import com.harnet.locationtest.services.PlacesService;

public class SettingsFragment extends Fragment {
    private SwitchCompat darkModeSwitch;
    private SwitchCompat muteSoundsSwitch;
    private SwitchCompat sqliteSwitch;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        darkModeSwitch = (SwitchCompat) view.findViewById(R.id.darkMode_switch);
        muteSoundsSwitch = (SwitchCompat) view.findViewById(R.id.muteSounds_switch);
        sqliteSwitch = (SwitchCompat) view.findViewById(R.id.SQLite_switch);

        SQLiteSwitch(sqliteSwitch);

        return view;
    }

    //set listener to SQLite switch
    public void SQLiteSwitch(SwitchCompat SQLiteSwitcher) {
        SQLiteSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlacesService.getInstance(getContext()).switchSQLite();
                boolean isSQLite = PlacesService.getInstance(getContext()).isSQLite();
                Toast.makeText(getContext(), "SQLite " + isSQLite, Toast.LENGTH_SHORT).show();
            }
        });
    }
}