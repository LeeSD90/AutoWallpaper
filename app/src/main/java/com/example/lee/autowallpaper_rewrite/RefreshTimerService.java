package com.example.lee.autowallpaper_rewrite;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class RefreshTimerService extends Service {
    private int interval;

    private Handler handler = new Handler();

    private Timer timer = null;

    public static final String UPDATE_WALL = "com.example.lee.autowallpaper_rewrite";
    Intent intent;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        timer.cancel();
        timer = new Timer();

        String intervalString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.interval_key), getString(R.string.default_interval));
        interval = Integer.parseInt(intervalString);

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

    @Override
    public void onDestroy() {
        stopService(intent);
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
                    Log.d("Timer", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.search_key), getString(R.string.default_search)));
                    WallpaperSetter.setNewWallpaper(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.search_key), getString(R.string.default_search)));
                    update();
                }
            });
        }
    }
}
