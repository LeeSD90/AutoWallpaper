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

public class AppPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{
    private final static String TAG = "AutoWallpaper";
    Preference wifiToggle, dataToggle, wallpaperButton, saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        wifiToggle = findPreference("wifi_only_key");
        dataToggle = findPreference("data_allowed_key");
        wallpaperButton = findPreference("wallpaper_button_key");
        saveButton = findPreference("save_button_key");

        wifiToggle.setOnPreferenceClickListener(this);
        wallpaperButton.setOnPreferenceClickListener(this);
        saveButton.setOnPreferenceClickListener(this);
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

    private boolean serviceEnabled(SharedPreferences sP){ return sP.getBoolean("service_toggle_key", false);}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent i = new Intent(getActivity(), AlarmReceiver.class);
        if(serviceEnabled(sharedPreferences)) {
            i.setAction(AlarmReceiver.SETUP);
        } else if (key.equals("service_toggle_key")){
            Log.d(TAG, "Service disabled, cancelling all alarms");
            i.setAction(AlarmReceiver.CANCEL);
        }
        getActivity().sendBroadcast(i);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch(preference.getKey()) {
            case "wallpaper_button_key":
                if(serviceEnabled(PreferenceManager.getDefaultSharedPreferences(getActivity()))){
                    Intent i = new Intent(getActivity(), AlarmReceiver.class);
                    i.setAction(AlarmReceiver.TRIGGER);
                    getActivity().sendBroadcast(i);
                }
                break;
            case "save_button_key":
                break;
            default:
                break;
        }
        return true;
    }
}
