package com.example.apple.lab_10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    WifiManager mwifiManager;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mwifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        Button btnCheck = (Button) findViewById(R.id.btnCheck5G);
        final TextView txt1 = (TextView) findViewById(R.id.txt1);
        TextView txt2 = (TextView) findViewById(R.id.txt2);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mwifiManager.is5GHzBandSupported())
                    txt1.setText("Your Device supports 5G!\n");
                else
                    txt1.setText("Your device doesn't support 5G.\n");
            }
        });


        String frequency = Double.toString(mwifiManager.getConnectionInfo().getFrequency() * 0.001); // convert MHz to GHz
        String speed = Integer.toString(mwifiManager.getConnectionInfo().getLinkSpeed());  // Mbps
        txt2.setText("Frequency: "+ frequency + "GHz" + "\nSpeed: "+ speed + "Mbps");

    }
}
