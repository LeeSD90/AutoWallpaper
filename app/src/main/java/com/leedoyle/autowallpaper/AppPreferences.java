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

public class AppPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference wallpaperButton = findPreference("wallpaper_button_key");
        wallpaperButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference){
                if(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("service_toggle_key", false)) {
                    if(preference.getSharedPreferences().getBoolean("Wifi_only_key", true)){
                        Log.d("AutoWallpaper", "Wifi only enabled");
                        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
                        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
                            Intent i = new Intent(getActivity(), AutoWallpaperService.class);
                            getActivity().startService(i);
                            return true;
                        } else return false;
                    } else {
                        Intent i = new Intent(getActivity(), AutoWallpaperService.class);
                        getActivity().startService(i);
                        return true;
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to obtain a new wallpaper, is the service enabled?", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(getActivity(), AlarmReceiver.class);
        if(sharedPreferences.getBoolean("service_toggle_key", false)) {
            i.setAction(AlarmReceiver.SETUP);
            i.putExtra("interval", Long.valueOf(sharedPreferences.getString("interval_key", "")));
        }
        else {
            i.setAction(AlarmReceiver.CANCEL);
        }
        getActivity().sendBroadcast(i);
    }

}
