package com.leedoyle.autowallpaper;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutoWallpaperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new AppPreferences()).commit();
    }
}
