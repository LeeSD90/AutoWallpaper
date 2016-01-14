package com.leedoyle.autowallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.Random;

/**
 * Created by Lee on 14/01/2016.
 */
public class PageParser {

    private static final String TAG = "PageParser";
    private Bitmap image;

    private void setImage(Bitmap image){
        this.image = image;
    }

    public Bitmap ParseForRandomWall(String page){
        Thread t = new Thread(){
            Bitmap image;
            String imageHLink = "";
            int upperLimit;
            @Override
            public void run(){
                try {
                    Document doc = Jsoup.connect("http://androidwallpape.rs/").get();
                    Element images = doc.getElementById("wallpapers");
                    Element firstImage = images.select("li").first();
                    upperLimit = Integer.parseInt(firstImage.attr("data-id")); //Set upper limit for RNG to the id of the latest image
                    Elements links = doc.getElementsByTag("li");
                    Random r = new Random();
                    int seed = r.nextInt((upperLimit + 1)- 1) + 1;          // Generate random number for wallpaper to pick
                    for(Element link : links) {
                        if (link.attr("data-id").equals(Integer.toString(seed))) {
                            Elements imageLinks = link.getElementsByTag("img");
                            for(Element imageLink : imageLinks){
                                Log.d(TAG, Integer.toString(seed));
                                Log.d(TAG, imageLink.toString());
                                imageHLink = imageLink.attr("data-original");
                                image = DownloadToBitmap(imageHLink);
                                setImage(image);
                            }
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try{
            t.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }

    private Bitmap DownloadToBitmap(String url){
        Bitmap image = null;
        try {
            URL newUrl = new URL(url);
            image = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return image;
    }
}
