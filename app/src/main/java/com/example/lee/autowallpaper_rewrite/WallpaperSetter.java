package com.example.lee.autowallpaper_rewrite;

import android.app.WallpaperManager;
import android.content.Context;

public class WallpaperSetter {

    private WallpaperSetter(){

    }

    // Get a new wallpaper with the given settings
    public static Boolean setNewWallpaper(Context c, String search) {
        try{
            WallpaperManager.getInstance(c).setBitmap(ImageParser.getWallpaper("https://www.google.no/search?tbm=isch&q=" + search));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
