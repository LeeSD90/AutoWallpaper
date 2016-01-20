package com.leedoyle.autowallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Lee on 19/01/2016.
 */
public class WallpaperAlarmSetupReceiver extends BroadcastReceiver {
    public static final String SETUP = "com.leedoyle.autowallpaper.setup";
    public static final String CANCEL = "com.leedoyle.autowallpaper.cancel";
    private static final String TAG = "AlarmSetupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, WallpaperAlarmReceiver.class);
        final PendingIntent pI = PendingIntent.getBroadcast(context, WallpaperAlarmReceiver.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);

        switch(intent.getAction()) {
            case SETUP:
                Long interval = intent.getLongExtra("interval", 60000);

                long initialTime = System.currentTimeMillis();
                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, initialTime, interval, pI);

                Toast.makeText(context, "Service enabled and settings applied", Toast.LENGTH_SHORT).show();
                break;
            case CANCEL:
                try{
                    alarm.cancel(pI);
                    Toast.makeText(context, "Service disabled", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Log.e(TAG, "Alarms not cancelled, perhaps none were set?");
            }
        }
    }
}
