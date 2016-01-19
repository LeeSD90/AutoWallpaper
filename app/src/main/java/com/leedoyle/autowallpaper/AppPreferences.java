package com.leedoyle.autowallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Lee on 19/01/2016.
 */
public class AppPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference applyButton = (Preference) findPreference(getString(R.string.apply_button));
        applyButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                Intent i = new Intent(getActivity(), WallpaperSetupReceiver.class);
                i.setAction(WallpaperSetupReceiver.ACTION);
                i.putExtra("interval", Long.valueOf(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("interval_key", "")));
                getActivity().sendBroadcast(i);
                return true;
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
        Toast.makeText(getActivity(), "Hello", Toast.LENGTH_LONG);
        Intent i = new Intent(getActivity(), WallpaperSetupReceiver.class);
        i.setAction(WallpaperSetupReceiver.ACTION);
        i.putExtra("interval", Long.valueOf(sharedPreferences.getString("interval_key", "")));
        getActivity().sendBroadcast(i);
    }
}
