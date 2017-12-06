package com.leedoyle.autowallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String SETUP = "com.leedoyle.autowallpaper.setup";
    public static final String CANCEL = "com.leedoyle.autowallpaper.cancel";
    public static final String TRIGGER = "com.leedoyle.autowallpaper.trigger";
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
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                if(!pref.getBoolean("service_toggle_key", false)){ Log.d(TAG, "Service disabled, doing nothing"); break; } //If the service is disabled on boot

                alarm.cancel(pI); //Cancel existing alarms

                Long interval = Long.valueOf(pref.getString("interval_key", ""));
                long initialTime = System.currentTimeMillis();
                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, initialTime, interval, pI);
                //Toast.makeText(context, "AutoWallpaper service enabled", Toast.LENGTH_SHORT).show();
                break;
            case CANCEL:
                try{
                    alarm.cancel(pI);
                    //Toast.makeText(context, "Autowallpaper service disabled", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Log.e(TAG, "Alarms not cancelled, perhaps none were set?");
                }
                break;
            case TRIGGER:
                Intent iA = new Intent(context, AutoWallpaperService.class);
                context.startService(iA);
                Toast.makeText(context, "Getting new wallpaper now...", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
