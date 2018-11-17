package com.example.lee.autowallpaper_rewrite;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class RefreshTimerService extends Service {
    private int interval;
    private String search;
    private HashMap<String, String> settings;

    private Handler handler = new Handler();

    private Timer timer = null;

    public static final String UPDATE_WALL = "com.example.lee.autowallpaper_rewrite";
    Intent intent;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        timer.cancel();
        timer = new Timer();

        settings = (HashMap) intent.getSerializableExtra("Settings");

        interval = Integer.parseInt(settings.get("Interval"));
        search = settings.get("Search");

        // TODO move this check?
        if (interval != 0) {
            timer.scheduleAtFixedRate(new refreshTimer(), 0, interval);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        intent = new Intent(UPDATE_WALL);
        // Cancel existing timer
        if(timer != null){
            timer.cancel();
        } else {
            timer = new Timer();
        }
    }

    private void update() {
        sendBroadcast(intent);
    }

    class refreshTimer extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Timer", "Running schedule now... " + Integer.toString(interval));
                    Log.d("Timer", "Using Term - " + search);
                    WallpaperSetter.setNewWallpaper(getApplicationContext(), settings);
                    update();
                }
            });
        }
    }
}
