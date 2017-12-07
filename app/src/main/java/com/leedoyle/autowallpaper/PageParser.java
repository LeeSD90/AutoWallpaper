package com.leedoyle.autowallpaper;

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

//TODO Class to form URLs so the user can select filters etc. per site?
public class PageParser {

    private static final String TAG = "PageParser";
    private static final String ANDROIDWALLPAPE_RS = "http://androidwallpape.rs/";
    private static final String GOOGLE = "https://www.google.com";
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
        String imageSearch = site;
        if(site.substring(0, GOOGLE.length()).equals(GOOGLE)){ site = GOOGLE ;}
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
            case GOOGLE:
                List<String> resultUrls = new ArrayList<String>();
                String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
                try {

                    doc = Jsoup.connect(imageSearch).userAgent(userAgent).referrer("https://www.google.com/").get();

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
                    r = new Random();
                    seed = r.nextInt((resultUrls.size() - 1) + 1) + 1;          //Generate random number for wallpaper to pick
                    imageHLink = resultUrls.get(seed);

                    for (String imageUrl : resultUrls) {
                        System.out.println(imageUrl);
                    }

                    Log.d(TAG, "Selected URL " + seed + " - " + imageHLink);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return imageHLink;
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
