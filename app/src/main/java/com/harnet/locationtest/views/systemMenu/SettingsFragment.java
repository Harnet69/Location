package com.harnet.locationtest.views.systemMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.harnet.locationtest.R;
import com.harnet.locationtest.models.AppSetting;
import com.harnet.locationtest.services.PlacesService;
import com.harnet.locationtest.viewmodels.AppSettingsActivityViewModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SettingsFragment extends Fragment {
    private Context context;

    private AppSettingsActivityViewModel appSettingsActivityViewModel;

    private SwitchCompat darkModeSwitch;
    private SwitchCompat muteSoundsSwitch;
    private SwitchCompat sqliteSwitch;
    private ProgressBar progressBar;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appSettingsActivityViewModel = new AppSettingsActivityViewModel();

        //MVVM approach
        appSettingsActivityViewModel.init();
        appSettingsActivityViewModel.getmAppSettings().observe(getActivity(), new Observer<List<AppSetting>>() {
            @Override
            public void onChanged(List<AppSetting> appSettings) {
                //TODO if settings was changed
                Log.i("MVVMinAction", "onChanged: ");
                setSettings();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        context = getContext();
        darkModeSwitch = (SwitchCompat) view.findViewById(R.id.darkMode_switch);
        muteSoundsSwitch = (SwitchCompat) view.findViewById(R.id.muteSounds_switch);
        sqliteSwitch = (SwitchCompat) view.findViewById(R.id.SQLite_switch);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        SQLiteSwitch(sqliteSwitch);



        return view;
    }

    //TODO make universal method for all switchers
    //set listener to SQLite switch
    public void SQLiteSwitch(SwitchCompat SQLiteSwitcher) {
        // set switcher on/off
        sqliteSwitch.setChecked(PlacesService.getInstance(context).isSQLite());

        SQLiteSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlacesService.getInstance(context).switchSQLite();
                boolean isSQLite = PlacesService.getInstance(context).isSQLite();
                if (isSQLite) {
                    Toast.makeText(context, "SQLite mode", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "SharedPreferences mode", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                //TODO make a migration from/to SQLite
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (!isSQLite) {
                            //MVVM
                            appSettingsActivityViewModel.switchSQLiteMode("SQLiteMode", false);

                            PlacesService.getInstance(context).migrateToSharedPreferences();
                        } else {
                            try {
                                //MVVM
                                appSettingsActivityViewModel.switchSQLiteMode("SQLiteMode", true);

                                PlacesService.getInstance(context).saveToSQLite();
                            } catch (IOException | SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        //delaying for spinner
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            }
        });
    }

    //setup app settings when they changed
    public void setSettings(){
        Log.i("MVVMinAction", "setSettings: ");
//        if(){
//
//        }
    }
}