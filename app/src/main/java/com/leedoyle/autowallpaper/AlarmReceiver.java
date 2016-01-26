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
public class AlarmReceiver extends BroadcastReceiver {
    public static final String SETUP = "com.leedoyle.autowallpaper.setup";
    public static final String CANCEL = "com.leedoyle.autowallpaper.cancel";
    public static final String TRIGGER = "com.leedoyle.autowallpaper.alarm";
    public static final int REQUEST_CODE = 99;
    private static final String TAG = "AlarmSetupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, this.getClass());
        i.setAction(TRIGGER);
        final PendingIntent pI = PendingIntent.getBroadcast(context, REQUEST_CODE, i, PendingIntent.FLAG_UPDATE_CURRENT);

        switch(intent.getAction()) {
            case SETUP:
            case "android.intent.action.BOOT_COMPLETED":
                Long interval = intent.getLongExtra("interval", 60000);

                long initialTime = System.currentTimeMillis();
                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, initialTime, interval, pI);
                if(intent.getStringExtra("key").equals("service_toggle_key")) Toast.makeText(context, "Service enabled", Toast.LENGTH_SHORT).show(); //Provide feedback if the enable button was toggled
                break;
            case CANCEL:
                try{
                    alarm.cancel(pI);
                    Toast.makeText(context, "Service disabled", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Log.e(TAG, "Alarms not cancelled, perhaps none were set?");
                }
                break;
            case TRIGGER:
                Intent iA = new Intent(context, AutoWallpaperService.class);
                context.startService(iA);
                break;
        }
    }
}
