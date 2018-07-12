package com.example.lee.autowallpaper_rewrite;

import android.app.WallpaperManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
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

        // Populate the search text in the nav drawer
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.nav_search);
        View subView = item.getActionView();
        TextView searchString = (TextView) subView.findViewById(R.id.searchStringView);
        searchString.setText("puppy");

        setPreview();

        Button newWallpaper = (Button) findViewById(R.id.newWallpaper);
        newWallpaper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                try {
                    WallpaperManager.getInstance(v.getContext()).setBitmap(ImageParser.getWallpaper("https://www.google.no/search?q=puppies&safe=off&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiW3tSZpZLcAhXDBZoKHcggB10Q_AUICigB&biw=2560&bih=1307"));
                    setPreview();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
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
            case R.id.nav_enabled:
                break;
            case R.id.nav_new_wall:
                break;
            case R.id.nav_save_wall:
                break;
            case R.id.nav_refresh:
                break;
            case R.id.nav_search:
                break;
            case R.id.nav_wifi:
                break;
            case R.id.nav_roaming:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Set up the wallpaper preview
    private void setPreview() {
        ImageView wallpaperPreviewArea = (ImageView) findViewById(R.id.previewImageView);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaper = wallpaperManager.getDrawable();
        wallpaperPreviewArea.setImageDrawable(wallpaper);
    }
}
