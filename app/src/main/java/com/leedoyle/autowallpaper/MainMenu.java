package com.leedoyle.autowallpaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner intervalSpinner;
    private Button applyButton;
    private int selectedInterval;   //TODO use later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        applyButton = (Button) findViewById(R.id.applyButton);
        intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);

        applyButton.setOnClickListener(this);
        intervalSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.interval_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalSpinner.setAdapter(adapter1);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.applyButton:
                applyChanges();
                break;
        }
    }

    private void applyChanges(){
        Intent i = new Intent(getApplicationContext(), WallpaperSetupReceiver.class);
        i.setAction(WallpaperSetupReceiver.ACTION);
        i.putExtra("interval", Long.valueOf(selectedInterval));
        sendBroadcast(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.intervalSpinner:
                switch(parent.getItemAtPosition(position).toString()){
                    case "1 minute":
                        selectedInterval = 60000;
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
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
