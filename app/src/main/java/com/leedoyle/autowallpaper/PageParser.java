package com.leedoyle.autowallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.WebView;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.Random;

//TODO Class to form URLs so the user can select filters etc. per site?
public class PageParser {

    private static final String TAG = "PageParser";
    private static final String ANDROIDWALLPAPE_RS = "http://androidwallpape.rs/";
    private static final String TODO_COM = "https://www.TODO.com/";
    private Bitmap image;

    private void setImage(Bitmap image){
        this.image = image;
    }

    public Bitmap getWallpaper(final String site){
        Thread t = new Thread(site){
            String imageHLink = "";

            @Override
            public void run(){
                try{
                    Document doc = Jsoup.connect(site).get();       //Get http page
                    //Log.d(TAG, doc.toString());
                    imageHLink = parsePage(site, doc);              //Get the hyperlink of a randomly selected wallpaper from the page
                    setImage(downloadToBitmap(imageHLink));         //Download the image to a bitmap
                } catch(Exception e){
                    Log.d(TAG, "Something bad happened");
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try{
            t.join();
        } catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }

    private String parsePage(String site, Document doc){
        String imageHLink = "";
        switch(site) {
            case ANDROIDWALLPAPE_RS:
                int upperLimit;
                Element images = doc.getElementById("wallpapers");
                Element firstImage = images.select("li").first();
                upperLimit = Integer.parseInt(firstImage.attr("data-id")); //Set upper limit for RNG to the id of the latest image
                Elements links = doc.getElementsByTag("li");
                Random r = new Random();
                int seed = r.nextInt((upperLimit + 1) - 1) + 1;          //Generate random number for wallpaper to pick
                for (Element link : links) {
                    if (link.attr("data-id").equals(Integer.toString(seed))) {
                        Elements imageLinks = link.getElementsByTag("img");
                        for (Element imageLink : imageLinks) {
                            Log.d(TAG, imageLink.toString());
                            imageHLink = imageLink.attr("data-original");
                            image = downloadToBitmap(imageHLink);
                            setImage(image);
                        }
                    }
                }
                return imageHLink;
            case TODO_COM:
                Elements anImage = doc.getElementsByClass("image");
                Log.d(TAG, anImage.toString());
                return "";
            default:
                return "";
        }
    }

    private Bitmap downloadToBitmap(String url){       //Gets a bitmap from an image url
        Bitmap image;
        try {
            URL newUrl = new URL(url);
            image = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
        }
        catch(Exception e){
            e.printStackTrace();
            Log.d(TAG, "Unable to obtain bitmap, no internet?");
            return null;
        }
        return image;
    }
}
