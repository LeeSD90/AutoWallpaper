package com.example.lee.autowallpaper_rewrite;

import android.app.WallpaperManager;
import android.content.Context;

import java.util.HashMap;

public class WallpaperSetter {

    private WallpaperSetter(){

    }

    // Get a new wallpaper with the given settings
    public static Boolean setNewWallpaper(Context c, HashMap<String, String> settings) {
        try{
            WallpaperManager.getInstance(c).setBitmap(ImageParser.getWallpaper("https://www.google.no/search?tbm=isch&q=" + settings.get("Search")));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
