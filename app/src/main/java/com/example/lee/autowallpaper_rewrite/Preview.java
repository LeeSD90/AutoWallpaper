package com.example.lee.autowallpaper_rewrite;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Preview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Interval spinner set up
        Spinner spinner = (Spinner) navigationView.getMenu().findItem(R.id.menu_interval).getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.intervals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // TODO add persistence
        // Populate the search text in the nav drawer
        setSearchString("puppy");

        // Set up the wallpaper preview
        setPreview();

        // New Wallpaper interface button
        Button newWallpaper = (Button) findViewById(R.id.newWallpaper);
        newWallpaper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                getNewWallpaper();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.menu_interval:
                break;
            case R.id.menu_new_wall:
                getNewWallpaper();
                break;
            case R.id.menu_save_wall:
                break;
            case R.id.menu_refresh:
                break;
            case R.id.menu_search:
                inputSearchString();
                break;
            case R.id.menu_wifi:
                break;
            case R.id.menu_roaming:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Get a new wallpaper with the given settings
    private void getNewWallpaper() {
        try{
            WallpaperManager.getInstance(getApplicationContext()).setBitmap(ImageParser.getWallpaper("https://www.google.no/search?q=" + getSearchString() + "&safe=off&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiW3tSZpZLcAhXDBZoKHcggB10Q_AUICigB&biw=2560&bih=1307"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        setPreview();
    }

    // Provide dialog for user to set the search string
    private void inputSearchString(){
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
                setSearchString(input.getText().toString());
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

    private void setSearchString(String text){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.menu_search);
        View subView = item.getActionView();
        TextView searchString = (TextView) subView.findViewById(R.id.searchStringView);
        searchString.setText(text);
    }

    private String getSearchString(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.menu_search);
        View subView = item.getActionView();
        TextView searchString = (TextView) subView.findViewById(R.id.searchStringView);
        return searchString.getText().toString();
    }

    // Set up the wallpaper preview
    private void setPreview() {
        ImageView wallpaperPreviewArea = (ImageView) findViewById(R.id.previewImageView);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaper = wallpaperManager.getDrawable();
        wallpaperPreviewArea.setImageDrawable(wallpaper);
    }
}
