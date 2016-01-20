package com.leedoyle.autowallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
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
        Log.d(TAG, "Getting new wallpaper");
        getRandomWallpaper(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("source_key", ""));
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
