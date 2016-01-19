package com.leedoyle.autowallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//TODO Add check for WiFi connection and app preference to download only over WiFi
//TODO Start adding additional source websites
//TODO Figure out how best to add options based on individual websites

/**
 * Created by Lee on 19/01/2016.
 */
public class WallpaperAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 99;
    public static final String ACTION = "com.leedoyle.autowallpaper.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoWallpaperService.class);
        context.startService(i);
    }
}
