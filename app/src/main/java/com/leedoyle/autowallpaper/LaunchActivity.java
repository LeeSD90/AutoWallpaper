package com.leedoyle.autowallpaper;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//TODO Some refactor again before continuing
//TODO Add lockscreen auto changer?
//TODO Maybe write something to log the details of wallpaper changes to help debugging
//TODO Test the data roaming settings
//TODO More source sites!

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On launch of the app, immediately go to app preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new AppPreferences()).commit();
    }
}
