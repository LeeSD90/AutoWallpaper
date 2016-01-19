package com.leedoyle.autowallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Lee on 19/01/2016.
 */
public class WallpaperSetupReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.leedoyle.autowallpaper.setup";

    @Override
    public void onReceive(Context context, Intent intent) {
        Long interval = intent.getLongExtra("interval", 60000);

        Intent i = new Intent(context, WallpaperAlarmReceiver.class);
        final PendingIntent pI = PendingIntent.getBroadcast(context, WallpaperAlarmReceiver.REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);

        long initialTime = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, initialTime, interval, pI);
    }
}
