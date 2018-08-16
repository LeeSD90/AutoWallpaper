package com.example.lee.autowallpaper_rewrite;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RefreshTimerService extends Service {
    public static final long INTERVAL = 10 * 1000; // 10 Seconds

    private Handler handler = new Handler();

    private Timer timer = null;

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

        timer.scheduleAtFixedRate(new Refresher(), 0, INTERVAL);
    }

    class Refresher extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), getDateTime(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private String getDateTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }
    }
}
