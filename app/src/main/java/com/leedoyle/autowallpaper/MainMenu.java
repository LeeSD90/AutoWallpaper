package com.leedoyle.autowallpaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainMenu extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner intervalSpinner;
    private Button testButton;
    private int selectedInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        testButton = (Button) findViewById(R.id.testButton);
        intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);

        testButton.setOnClickListener(this);
        intervalSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.interval_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(adapter1);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.intervalSpinner:
                switch(parent.getItemAtPosition(position).toString()){
                    case "10 seconds (Test!!!)":
                        selectedInterval = 10000;
                        break;
                    case "10 minutes":
                        selectedInterval = 600000;
                        break;
                    case "30 minutes":
                        selectedInterval = 1800000;
                        break;
                    case "1 hour":
                        selectedInterval = 3600000;
                        break;
                    case "3 hours":
                        selectedInterval = 10800000;
                        break;
                    case "6 hours":
                        selectedInterval = 21600000;
                        break;
                    case "Daily":
                        selectedInterval = 86400000;
                        break;
                }
                Intent i = new Intent(getBaseContext(), AutoWallpaperService.class);
                i.putExtra("interval", Integer.toString(selectedInterval));
                startService(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
