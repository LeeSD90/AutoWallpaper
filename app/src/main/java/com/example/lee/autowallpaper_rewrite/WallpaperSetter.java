package com.example.lee.autowallpaper_rewrite;

import android.app.WallpaperManager;
import android.content.Context;

import java.util.HashMap;

public class WallpaperSetter {

    private WallpaperSetter(){

    }
    // TODO Implement custom settings
    // TODO Include ability to base aspect ratio and size on device resolution/dimensions?
    // Get a new wallpaper with the given settings
    public static Boolean setNewWallpaper(Context c, HashMap<String, String> settings) {
        try{
            String search = settings.get("Search");
            WallpaperManager.getInstance(c).setBitmap(ImageParser.getWallpaper("https://www.google.no/search?tbm=isch&q=" + search //Search String
                                                                                            + "&tbs="
                                                                                            + "iar:t," //Aspect Ratio
                                                                                            + "isz:lt,islt:qsvga" //Image size
            ));
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
