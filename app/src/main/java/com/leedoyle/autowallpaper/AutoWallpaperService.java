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
            String siteString;
            String searchString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("search_key", "NULL");
            searchString = searchString.replaceAll(" ", "+");
            if(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("source_key", "NULL").equals("Google Images")) {
                siteString = "https://www.google.com/search?tbm=isch&tbs=iar:t,isz:lt,islt:xga&q=" + searchString ;
                Log.d(TAG, siteString);
            }
            else {
                siteString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("source_key", "");
            }
            setRandomWallpaper(siteString);
        } else Log.d(TAG, "No suitable connection is available to download over");
    }

    private boolean CheckConnectionStatus(SharedPreferences sharedPreferences){
        Log.d(TAG, "Checking connection settings");
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){ Log.d(TAG, "Wifi connection detected"); return true;}     // If a WiFi connection is present
        else if(sharedPreferences.getBoolean("data_allowed_key", false) && !sharedPreferences.getBoolean("Wifi_only_key", true)){     // If data roaming is enabled and no WiFi is present
            Log.d(TAG, "Data roaming is enabled and no WiFi is present");
            return cm.getActiveNetworkInfo().isRoaming();                                                                             // Return roaming connectivity status
        }
        else return false;                                                                                                            // If a WiFi connection is not present, and data roaming is either disabled or unavailable
    }

    public boolean setRandomWallpaper(String site){      //Attempts to retrieve a random wallpaper from the selected website
        try {
            Log.d(TAG, "About to parse");
            Bitmap image = pageParser.getWallpaper(site);
            if(image != null) {
                WallpaperManager.getInstance(this).setBitmap(image);
                Log.d(TAG, "Done parsing");
                return true;
            } else Log.d(TAG, "Image could not be retrieved");
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
