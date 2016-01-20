package com.leedoyle.autowallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class AutoWallpaperService extends IntentService {

    private static final String TAG = "AutoWallpaperService";
    private PageParser pageParser;

    public AutoWallpaperService(){
        super("AutoWallpaperService");
        pageParser = new PageParser();
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Log.d(TAG, "Service running");

        if(CheckConnectionStatus(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()))){
            Log.d(TAG, "Getting new wallpaper");
            getRandomWallpaper(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("source_key", ""));
        } else Log.d(TAG, "Download over WiFi only enabled, but no Wifi is present");
    }

    private boolean CheckConnectionStatus(SharedPreferences sharedPreferences){
        Log.d(TAG, "Checking connection settings");
        if(sharedPreferences.getBoolean("Wifi_only_key", true)){
            Log.d(TAG, "Wifi only enabled");
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        } else return true;
    }

    public Bitmap getRandomWallpaper(String site){
        try {
            Log.d(TAG, "About to parse");
            Bitmap image = pageParser.ParseForRandomWall(site);
            if(image != null) {
                WallpaperManager.getInstance(this).setBitmap(image);
            }
            Log.d(TAG, "Done parsing");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
