package com.leedoyle.autowallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
