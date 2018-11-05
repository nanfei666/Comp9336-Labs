package com.example.jackchu.lab4;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    WifiP2pManager wifiP2pManager;
    Context context;
    WifiP2pManager.Channel channel;
    WifiP2pManager.ActionListener listner;
    ArrayList<WifiP2pDevice> serverList = new ArrayList<WifiP2pDevice>();
    private final IntentFilter intentFilter = new IntentFilter();
    List<String> peersInfo = new ArrayList<>();
    ArrayAdapter mArrayAdapter;
    ListView list1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnWifiDirect = (Button) findViewById(R.id.btnWifiDirect);
        final Button btnPeerDiscovery = (Button) findViewById(R.id.btnPeer);
        final TextView txtWifiDirect = (TextView) findViewById(R.id.txtWifiDirect);
        list1 = (ListView) findViewById(R.id.list_view);
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);

        btnWifiDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckWifiDirect()) {
                    txtWifiDirect.setText("Wifi direct is available.");
                    btnPeerDiscovery.setVisibility(View.VISIBLE);
                }
                else
                    txtWifiDirect.setText("Wifi direct is not available.");
            }
        });

        btnPeerDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "scanning...", Toast.LENGTH_SHORT).show();

                ScanPeers();
            }
        });
    }
//    @Override
//    protected void onListItemClick(ListView l, View v, final int position, long id) {
//        // TODO Auto-generated method stub
//        super.onListItemClick(l, v, position, id);
//        System.out.println("list clicked "+position+" --id "+id);
//        System.out.println("compare-> "+deviceList.get(position)+list1.getItemAtPosition(position).getClass().getName()+" id"+id);
//        btAdapter.cancelDiscovery();
//        new AlertDialog.Builder(this).setMessage("Connect?").setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//                try {
//                    Method method = deviceList.get(position).getClass().getMethod("createBond", (Class[]) null);
//                    method.invoke(deviceList.get(position), (Object[]) null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).setNegativeButton("Cancel", null).show();
//        /*try {
//            Method method = deviceList.get(position).getClass().getMethod("createBond", (Class[]) null);
//            method.invoke(deviceList.get(position), (Object[]) null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }*/
//    }



    public boolean CheckWifiDirect() {
        wifiP2pManager = (WifiP2pManager) getApplicationContext().getSystemService(WIFI_P2P_SERVICE);
        if(wifiP2pManager.WIFI_P2P_STATE_ENABLED != 2)
            return false;
        return true;
    }


    public void ScanPeers() {
        wifiP2pManager = (WifiP2pManager) getApplicationContext().getSystemService(WIFI_P2P_SERVICE);

        WifiP2pManager.Channel channel = wifiP2pManager.initialize(MainActivity.this, getMainLooper(), null);

        WifiP2pManager.ActionListener actionListener = new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {
                if (i == 0) {
                    Toast.makeText(MainActivity.this, "Discovery Failed likely due to bad Wi-fi; please retry", Toast.LENGTH_SHORT).show();
                } else if (i == 2) {
                    Toast.makeText(MainActivity.this, "Discovery failed due to bad Wi-fi state; turn Wi-fi on and off, then retry", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Discovery failed with reason code: " + i, Toast.LENGTH_SHORT).show();
                }
            }
        };

        BroadcastReceiver mReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

                }else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                    Toast.makeText(getApplicationContext(), "WIFI_P2P_PEERS_CHANGED_ACTION", Toast.LENGTH_SHORT).show();
                    WifiP2pDeviceList list = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                    final List<WifiP2pDevice> devices = new ArrayList<>();

                    peersInfo.clear();
                    for (WifiP2pDevice d : list.getDeviceList()) { //...
                        devices.add(d);
                        String temp = d.deviceName + ": \n" + d.deviceAddress + "\n";
                        peersInfo.add(temp);
                    }
                    list1.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.text_list, R.id.textlist, peersInfo));
                    list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            connectDevice(devices.get(i));
                        }
                    });
                }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                    // Respond to new connection or disconnections
                    Toast.makeText(getApplicationContext(), "WIFI_P2P_CONNECTION_CHANGED_ACTION", Toast.LENGTH_SHORT).show();

                    if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                        int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                        if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                            // Wifi P2P is enabled
                        } else {
                            // Wi-Fi P2P is not enabled
                        }
                    }
                }
            }
        };

        IntentFilter mIntentFilter = new IntentFilter(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        registerReceiver(mReceiver, mIntentFilter);

        wifiP2pManager.discoverPeers(channel, actionListener);

    }

    public void connectDevice(WifiP2pDevice device){
        WifiP2pConfig config = new WifiP2pConfig();

        config.deviceAddress = device.deviceAddress;

        Toast.makeText(getApplicationContext(), "Connecting device "+device.deviceName, Toast.LENGTH_SHORT).show();

        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Successfully connected.", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(getApplicationContext(), "Connection failed.", Toast.LENGTH_SHORT);
            }
        });
    }
}
