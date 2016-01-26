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
    Preference wifiToggle;
    Preference dataToggle;
    Preference wallpaperButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        wifiToggle = findPreference("wifi_only_key");
        dataToggle = findPreference("data_allowed_key");
        wallpaperButton = findPreference("wallpaper_button_key");

        wifiToggle.setOnPreferenceClickListener(this);
        wallpaperButton.setOnPreferenceClickListener(this);
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
        Intent i = new Intent(getActivity(), AlarmReceiver.class);
        if(sharedPreferences.getBoolean("service_toggle_key", false)) {
            i.setAction(AlarmReceiver.SETUP);
            i.putExtra("interval", Long.valueOf(sharedPreferences.getString("interval_key", "")));
            i.putExtra("key", key);
        }
        else {
            i.setAction(AlarmReceiver.CANCEL);
        }
        getActivity().sendBroadcast(i);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch(preference.getKey()){
            case "wallpaper_button_key":
                if(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("service_toggle_key", false)) {
                    if(preference.getSharedPreferences().getBoolean("wifi_only_key", true)){
                        Log.d(TAG, "Wifi only enabled");
                        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
                        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
                            Intent i = new Intent(getActivity(), AutoWallpaperService.class);
                            getActivity().startService(i);
                            break;
                        } else Log.d(TAG, "No suitable connection is available to download over"); break;
                    } else {
                        Intent i = new Intent(getActivity(), AutoWallpaperService.class);
                        getActivity().startService(i);
                        break;
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to obtain a new wallpaper, is the service enabled?", Toast.LENGTH_SHORT).show();
                    break;
                }
            default:
                break;
        }
        return true;
    }
}
