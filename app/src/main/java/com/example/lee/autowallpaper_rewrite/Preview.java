package com.example.lee.autowallpaper_rewrite;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

// TODO repeat failed new wallpaper attempts
// TODO implement the rest of sharedPrefs
// TODO additional mobile friendly parameters
// TODO Refactor function names

public class Preview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 999;

    SharedPreferences sharedPreferences;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkPermissionAndUpdateWallpaperPreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Interval spinner set up
        Spinner spinner = (Spinner) navigationView.getMenu().findItem(R.id.menu_interval).getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.interval_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = getResources().getStringArray(R.array.interval_array_values)[i];
                setStoredInterval(selected);

                setNewRefreshTimer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String compare = getInterval();
        adapter = ArrayAdapter.createFromResource(this, R.array.interval_array_values, android.R.layout.simple_spinner_item);
        if(compare != null) {
            spinner.setSelection(adapter.getPosition(compare));
        }

        // Set up the wallpaper preview
        checkPermissionAndUpdateWallpaperPreview();


        // Get Settings from sharedprefs
        refreshInterfaceSettings();

        // New Wallpaper interface button
        Button newWallpaper = findViewById(R.id.newWallpaper);
        newWallpaper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                getNewWallpaper();
            }
        });
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.menu_new_wall:
                getNewWallpaper();
                break;
            case R.id.menu_save_wall:
                break;
            case R.id.menu_search:
                openSearchStringInput();
                break;
            case R.id.menu_wifi:
                break;
            case R.id.menu_roaming:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getNewWallpaper(){
        if(WallpaperSetter.setNewWallpaper(getApplicationContext(), getSettings())){
            checkPermissionAndUpdateWallpaperPreview();
        }
    }

    private void setNewRefreshTimer() {
        Intent i = new Intent(this, RefreshTimerService.class);
        i.putExtra("Settings", getSettings());
        this.startService(i);
    }

    // Provide dialog for user to set the search string
    private void openSearchStringInput(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search Term");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(getSearchString());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Set the text
                setStoredSearchString(input.getText().toString());
                setNewRefreshTimer();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Reset the text
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void setStoredInterval(String interval){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.interval_key), interval);
        editor.commit();
    }

    private String getInterval(){
        return sharedPreferences.getString(getString(R.string.interval_key), getString(R.string.default_interval));
    }

    private void setStoredSearchString(String text){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.search_key), text);
        editor.commit();
        refreshInterfaceSettings();
    }

    private String getSearchString(){
        return sharedPreferences.getString(getString(R.string.search_key), getString(R.string.default_search));
    }

    private HashMap<String, String> getSettings(){
        HashMap<String, String> settings = new HashMap<String, String>();
        settings.put("Search", getSearchString());
        settings.put("Interval", getInterval());
        return settings;
    }

    private void refreshInterfaceSettings() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.menu_search);
        View subView = item.getActionView();

        TextView searchString = subView.findViewById(R.id.searchStringView);
        searchString.setText(getSearchString());
    }

    private void checkPermissionAndUpdateWallpaperPreview() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE);

                return;
            } else {
                updateWallpaperPreview();
            }
        } else {
            updateWallpaperPreview();
        }
    }

    // Update wallpaper preview box
    private void updateWallpaperPreview() {
        ImageView wallpaperPreviewArea = findViewById(R.id.previewImageView);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaper = wallpaperManager.getDrawable();
        wallpaperPreviewArea.setImageDrawable(wallpaper);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    updateWallpaperPreview();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(RefreshTimerService.UPDATE_WALL));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
