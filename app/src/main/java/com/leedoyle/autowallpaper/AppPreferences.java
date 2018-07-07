package com.leedoyle.autowallpaper;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{
    private final static String TAG = "AutoWallpaper";
    Preference wifiToggle, dataToggle, wallpaperButton, saveButton, searchBox, siteSearchBox, preview;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        wifiToggle = findPreference("wifi_only_key");
        dataToggle = findPreference("data_allowed_key");
        wallpaperButton = findPreference("wallpaper_button_key");
        saveButton = findPreference("save_button_key");
        searchBox = findPreference("search_key");
        siteSearchBox = findPreference("url_search_key");

        updateUI();

        wifiToggle.setOnPreferenceClickListener(this);
        wallpaperButton.setOnPreferenceClickListener(this);
        saveButton.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private boolean serviceEnabled(SharedPreferences sP){ return sP.getBoolean("service_toggle_key", false);}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent i = new Intent(getActivity(), AlarmReceiver.class);

        if(serviceEnabled(sharedPreferences)) {
            Log.d(TAG, "Service enabled, setting up alarm");
            i.setAction(AlarmReceiver.SETUP);
        }  else if (key.equals("service_toggle_key")){
            Log.d(TAG, "Service disabled, cancelling all alarms indefinitely");
            i.setAction(AlarmReceiver.CANCEL);
        }

        if(!checkURL()){
            Toast.makeText(getActivity().getApplicationContext(), "Malformed URL entered, please enter a valid URL!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Malformed URL entered!");
        }

        updateUI();
        getActivity().sendBroadcast(i);
    }

    private boolean checkURL(){
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        String urlString = "http://" + sp.getString("url_search_key", "NULL");
        Log.d(TAG, urlString);
        if(URLUtil.isHttpUrl(urlString)){
            return true;
        }
        else{
            siteSearchBox.setSummary("Enter a website URL to search within through Google");
            return false;
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch(preference.getKey()) {
            case "wallpaper_button_key":
                if(serviceEnabled(PreferenceManager.getDefaultSharedPreferences(getActivity()))){
                    Intent i = new Intent(getActivity(), AlarmReceiver.class);
                    i.setAction(AlarmReceiver.TRIGGER);
                    getActivity().sendBroadcast(i);
                }
                break;
            case "save_button_key":
                try {
                    saveWallpaper();
                }
                catch(Exception e){
                        e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void updateUI(){
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        searchBox.setEnabled(sp.getString("source_key", "NULL").equals("Google Images"));
        siteSearchBox.setEnabled(sp.getString("source_key", "NULL").equals("Google Images"));

        EditTextPreference etp = (EditTextPreference) searchBox;
        searchBox.setSummary(etp.getText());

        etp = (EditTextPreference) siteSearchBox;
        siteSearchBox.setSummary(etp.getText());
    }

    private void saveWallpaper() throws Exception{
        Bitmap wallpaper = ((BitmapDrawable)WallpaperManager.getInstance(getActivity()).getDrawable()).getBitmap();

        String dirRoot = Environment.getExternalStorageDirectory().toString();
        File wallpaperDir = new File(dirRoot + "/wallpapers");
        wallpaperDir.mkdirs();
        String n = new SimpleDateFormat("ddMMyy_HHmmss").format(Calendar.getInstance().getTime()); // Gets the date & time to use as a name for the file
        String fileName = "wallpaper_" + n + ".jpg";
        File file = new File(wallpaperDir, fileName);
        //while(file.exists()) file = new File(wallpaperDir, "Image_" + ran.nextInt(90000) + ".jpg");
        FileOutputStream fos = new FileOutputStream(file);
        try{
            wallpaper.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            MediaScannerConnection.scanFile(getActivity(), new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener(){
                public void onScanCompleted(String path, Uri uri){
                    Log.i(TAG, "Scan completed.");
                }
            });
            Toast.makeText(getActivity().getApplicationContext(), "Wallpaper saved as " + fileName, Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            fos.close();
        }
    }
}
