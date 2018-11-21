package com.example.lee.autowallpaper_rewrite;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

public class RefreshReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       HashMap<String, String> settings = (HashMap) intent.getSerializableExtra("Settings");

       if(intent.getBooleanExtra("DEBUG", false)){
           Intent aI = new Intent(context, RefreshReceiver.class);
           aI.putExtra("Settings", settings);
           aI.putExtra("DEBUG", true);
           PendingIntent pI = PendingIntent.getBroadcast(context, 0, aI, PendingIntent.FLAG_UPDATE_CURRENT);
           AlarmManager aM = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
           aM.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, pI);
       }

       Log.d("Timer", "Running schedule now... " + settings.get("Interval"));
    }
}
