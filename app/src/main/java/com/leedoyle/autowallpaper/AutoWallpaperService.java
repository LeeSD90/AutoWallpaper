package com.leedoyle.autowallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
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
        getRandomWallpaper();
    }

    private Bitmap getRandomWallpaper(){
        try {
            String htmlPage = "";
            Log.d(TAG, "About to parse");
            Bitmap image = pageParser.ParseForRandomWall(htmlPage);
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

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
