package com.example.apple.lab_09_task_03;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;





public class MainActivity extends AppCompatActivity {

    private Button bt, buttonSave;
    private Boolean scanClicked = false;
    private ToggleButton tb;
    private ListView lv;

    private WifiManager wifi;
    private WifiScanReceiver wifiRec;

    private String wifis[];

    final Context context = this;
    private List<String> wifiList = new ArrayList<String>();
    private List<String> forFile = new ArrayList<String>();

    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = (Button) findViewById(R.id.button);
        tb = (ToggleButton) findViewById(R.id.toggleButton);
        lv = (ListView) findViewById(R.id.listView);

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiRec = new WifiScanReceiver();

        bt.setEnabled(wifi.isWifiEnabled());
        tb.setChecked(wifi.isWifiEnabled());

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanClicked = true;
            }
        });

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.setWifiEnabled(!wifi.isWifiEnabled());
                tb.setChecked(!wifi.isWifiEnabled());
                bt.setEnabled(!wifi.isWifiEnabled());
                lv.setAdapter(null);
            }
        });


        buttonSave = (Button) findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File myFile = new File("/sdcard/WIFILIST.txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    for (String item : forFile) {
                        Toast.makeText(getBaseContext(),
                                item,
                                Toast.LENGTH_SHORT).show();
                        myOutWriter.append(item);
                    }

                    myOutWriter.close();
                    fOut.close();
                    Toast.makeText(getBaseContext(),
                            "Done writing SD 'WIFILIST.txt'",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    protected void onPause() {
        unregisterReceiver(wifiRec);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiRec, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            if (scanClicked) {

                List<ScanResult> wifiScanList = wifi.getScanResults();

                if (wifiScanList.size() == 0){
                    Toast.makeText(MainActivity.this,"No Networks Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create Comparator to sort by level
                Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult lhs, ScanResult rhs) {
                        return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
                    }
                };

                // Apply Comparator and sort
                Collections.sort(wifiScanList, comparator);

                wifiList = new ArrayList<String>();
                forFile = new ArrayList<String>();
                wifis = new String[wifiScanList.size()];


                List<String> alreadySeen = new ArrayList<String>();
                int x = 0;
                for (int i = 0; i < wifiScanList.size(); i++) {
                    if (!alreadySeen.contains(wifiScanList.get(i).SSID) && wifiScanList.get(i).SSID.length() > 0) {
                        alreadySeen.add(wifiScanList.get(i).SSID);
                        wifis[x] = wifiScanList.get(i).SSID + " (" + wifiScanList.get(i).BSSID + ") " + wifiScanList.get(i).level;
                        wifiList.add(wifiScanList.get(i).SSID + " (" + wifiScanList.get(i).BSSID + ") " + wifiScanList.get(i).level);
                        x++;

                        //-------------------------------lab 10 part 3-------------------
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));
                        String outputString = "";
                        outputString += dateFormat.format(date)+"\n";
                        outputString += "(-33.917347, 151.231268)\n";
                        outputString += "SSID: "+wifiScanList.get(i).SSID+"\n";
                        outputString += "Signal: "+wifiScanList.get(i).level+"\n";
                        outputString += "----------------------------------------\n";
                        forFile.add(outputString);
                    }
                }

                lv.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, wifiList));
                scanClicked=false;



            }
        }
    }


}