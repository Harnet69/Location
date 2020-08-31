package com.harnet.locationtest.views.systemMenu;

import android.content.Context;
import android.os.AsyncTask;
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
    private Context context;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        context = getContext();
        darkModeSwitch = (SwitchCompat) view.findViewById(R.id.darkMode_switch);
        muteSoundsSwitch = (SwitchCompat) view.findViewById(R.id.muteSounds_switch);
        sqliteSwitch = (SwitchCompat) view.findViewById(R.id.SQLite_switch);

        SQLiteSwitch(sqliteSwitch);

        return view;
    }

    //TODO make universal method for all switchers
    //set listener to SQLite switch
    public void SQLiteSwitch(SwitchCompat SQLiteSwitcher) {
        SQLiteSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlacesService.getInstance(context).switchSQLite();
                boolean isSQLite = PlacesService.getInstance(context).isSQLite();

                //TODO make a migration from/to SQLite
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        //TODO make a spinner to show migration is work
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if(!isSQLite) {
                            PlacesService.getInstance(context).migrateToSharedPreferences();
                        }else{
                            PlacesService.getInstance(context).migrateToSQLite();
                        }
                        return null;
                    }
                }.execute();

                Toast.makeText(context, "SQLite " + isSQLite, Toast.LENGTH_SHORT).show();
            }
        });
    }
}