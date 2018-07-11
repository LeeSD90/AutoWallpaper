package com.example.lee.autowallpaper_rewrite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ImageParser {

    private static final String TAG = "ImageParser";
    private static final String GOOGLE = "https://www.google.com";

    private static Bitmap image;

    private ImageParser(){

    }

    // Accepts a google image search URL and returns a random image
    public static Bitmap getWallpaper(final String searchURL) {
        Thread t = new Thread(searchURL){
            String imageURL;

            @Override
            public void run(){
                try{
                    imageURL = selectImage(searchURL);                    // Get the hyperlink of a randomly selected wallpaper from the page
                    image = (getBitmapFromURL(imageURL));           // Download the image to a bitmap
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

    private static String selectImage(String searchURL){
        String imageURL = "";
        Document doc;

        List<String> resultUrls = new ArrayList<String>();
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
        try {

            doc = Jsoup.connect(searchURL).userAgent(userAgent).referrer(GOOGLE).get();
            Log.d(TAG, doc.toString());
            Elements elements = doc.select("div.rg_meta");

            JSONObject jsonObject;
            for (Element element : elements) {
                if (element.childNodeSize() > 0) {
                    jsonObject = (JSONObject) new JSONParser().parse(element.childNode(0).toString());
                    resultUrls.add((String) jsonObject.get("ou"));
                }
            }

            System.out.println("number of results: " + resultUrls.size());
            Log.d(TAG, resultUrls.get(0));
            Random r = new Random();
            int seed = r.nextInt((resultUrls.size() - 1) + 1) + 1;          // Generate random number for wallpaper to pick
            imageURL = resultUrls.get(seed);

            for (String imageUrl : resultUrls) {
                System.out.println(imageUrl);
            }

            Log.d(TAG, "Selected URL " + seed + " - " + imageURL);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageURL;
    }

    // Gets a bitmap from an image url
    private static Bitmap getBitmapFromURL(String url){
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
