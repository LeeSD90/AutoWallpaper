package com.example.lee.autowallpaper_rewrite;

import android.app.WallpaperManager;
import android.content.Context;

public class WallpaperSetter {


    private WallpaperSetter(){

    }

    // TODO Own thread?
    // Get a new wallpaper with the given settings
    public static Boolean setNewWallpaper(Context c, String search) {
        try{
            WallpaperManager.getInstance(c).setBitmap(ImageParser.getWallpaper("https://www.google.no/search?q=" + search + "&safe=off&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiW3tSZpZLcAhXDBZoKHcggB10Q_AUICigB&biw=2560&bih=1307"));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
