package com.leedoyle.autowallpaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    ImageView testView;
    //BitmapDrawable bitmapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        //bitmapDrawable = (BitmapDrawable) wallManager.getDrawable();

        Button testButton = (Button) findViewById(R.id.testButton);
        testView = (ImageView) findViewById(R.id.testView);

        testButton.setOnClickListener(this);
    }

    public void startService(View view){
        startService(new Intent(getBaseContext(), AutoWallpaperService.class));
    }

    public void stopService(View view){
        stopService(new Intent(getBaseContext(), AutoWallpaperService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.testButton:
                startService(v);
                //Bitmap bitmap = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                //((ImageView) findViewById(R.id.testView)).setImageBitmap(bitmap);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
