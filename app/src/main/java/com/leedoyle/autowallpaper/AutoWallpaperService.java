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
    private boolean running;
    private int interval = 10000;

    public AutoWallpaperService(){
        pageRetriever = new PageRetriever();
        pageParser = new PageParser();
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
        running = true;

        if(intent.getExtras() != null){
            interval = Integer.parseInt(intent.getStringExtra("interval"));
        }

        Log.d(TAG, Integer.toString(interval));

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
                while(running)
                {
                    try{
                        Thread.sleep(interval);     // Wait (10min = 600000)
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
        running = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
