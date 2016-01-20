package com.leedoyle.autowallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Lee on 19/01/2016.
 */
public class AppPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference applyButton = (Preference) findPreference("restart_key");
        applyButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                if(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("service_toggle_key", false)) {
                    Intent i = new Intent(getActivity(), WallpaperAlarmSetupReceiver.class);
                    i.setAction(WallpaperAlarmSetupReceiver.SETUP);
                    i.putExtra("interval", Long.valueOf(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("interval_key", "")));
                    getActivity().sendBroadcast(i);
                    return true;
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to restart the service, is it enabled?", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent i = new Intent(getActivity(), WallpaperAlarmSetupReceiver.class);
        if(sharedPreferences.getBoolean("service_toggle_key", false)) {
            i.setAction(WallpaperAlarmSetupReceiver.SETUP);
            i.putExtra("interval", Long.valueOf(sharedPreferences.getString("interval_key", "")));
        }
        else {
            i.setAction(WallpaperAlarmSetupReceiver.CANCEL);
        }
        getActivity().sendBroadcast(i);
    }

}
