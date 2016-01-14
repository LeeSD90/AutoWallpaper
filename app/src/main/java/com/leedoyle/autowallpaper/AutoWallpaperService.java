package com.leedoyle.autowallpaper;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class AutoWallpaperService extends Service {

    private static final String TAG = "AutoWallpaperService";
    private PageRetriever pageRetriever;
    private PageParser pageParser;
    private WallpaperManager wallManager;

    public AutoWallpaperService(){
        pageRetriever = new PageRetriever();
        pageParser = new PageParser();
        //wallManager = WallpaperManager.getInstance(this);
    }

    private void setNewWallpaper() {
        Log.d(TAG, "Getting wallpaper");
        Bitmap wallpaper = getRandomWallpaper();
    }

    private Bitmap getRandomWallpaper(){
        try {
            String htmlPage = "";
            Log.d(TAG, "About to parse");
            Bitmap image = pageParser.ParseForRandomWall(htmlPage);
            WallpaperManager.getInstance(this).setBitmap(image);
            Log.d(TAG, "Done parsing");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Log.d(TAG, "Service Started.");
        final Handler h = new Handler(){
            @Override
            public void handleMessage(Message m){   //Handle recieved messages
                super.handleMessage(m);
                setNewWallpaper();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    try{
                        Thread.sleep(Math.round(10000));     // Wait (10min = 600000)
                        h.sendEmptyMessage(0);  //Send message to Handler
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startID);
    }

    @Override
    public void onDestroy(){

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
