package com.example.apple.lab_05_task02;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean gps_ok, wifi_ok;
    private TextView  text_view2;
    String text;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_view2 = (TextView)findViewById(R.id.textView2);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    begin_test(view);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void check_GPS() {

        LocationManager manager;
        manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        gps_ok = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void check_wifi() {

        WifiManager wifi;
        wifi = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        wifi_ok = wifi.isWifiEnabled();

    }


    public void begin_test(View source) throws InterruptedException {

        int before_level, after_level;
        float before, after;
        String show_text;

        check_GPS();
        check_wifi();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        before_level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        before = before_level / (float)scale;
        text_view2.setText("Please waite .....");
        Thread.sleep(5000);




        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null, ifilter);
        after_level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        after = after_level / (float)scale;

        if (!gps_ok && !wifi_ok) {
            show_text = "Normal usage of mobile phone for 10 minutes: \n\n";
        }
        else if (gps_ok && !wifi_ok) {
            show_text = "Using GPS for 1 minutes: \n\n";
        }
        else if (!gps_ok && wifi_ok){
            show_text = "Using Wi-Fi for 1 minutes: \n\n";
        }
        else {
            show_text = "Using Wi-Fi and GPS for 1 minutes: \n\n";
        }

        show_text += "initial level of battery: " + before * 100 + "%.\n\n";
        show_text += "Final level: " + after * 100 + "%.\n\n";
        show_text += "Consumed battery: " + (before - after) * 100 + "%.\n";

        text_view2.setText(show_text);

    }
}
