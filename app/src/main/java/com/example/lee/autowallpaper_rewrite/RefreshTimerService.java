package com.example.lee.autowallpaper_rewrite;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class RefreshTimerService extends Service {
    private int interval;

    private Handler handler = new Handler();

    private Timer timer = null;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        timer.cancel();
        timer = new Timer();
        interval = (int) intent.getExtras().get("interval");

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
        // Cancel existing timer
        if(timer != null){
            timer.cancel();
        } else {
            timer = new Timer();
        }
    }

    class refreshTimer extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), Integer.toString(interval), Toast.LENGTH_SHORT).show();
                    Log.d("Timer", "Running schedule now...");
                }
            });
        }
    }
}
