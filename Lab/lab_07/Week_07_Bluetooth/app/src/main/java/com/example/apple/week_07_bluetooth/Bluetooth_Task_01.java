package com.example.apple.week_07_bluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Bluetooth_Task_01 extends ListActivity {

    private Button buttonstatus;
    private Button buttondiscover;
    private TextView text1;
    private ListView list1;
    BluetoothAdapter btAdapter;
    //ArrayAdapter mArrayAdapter;
    private BroadcastReceiver mReceiver;
    Intent bluemanager;
    ArrayList<String> resultList = new ArrayList<String>();
    ArrayList<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth__task_01);
        text1 = (TextView) findViewById(R.id.text1);
        list1 = (ListView) findViewById(android.R.id.list);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mReceiver = new BlueReceiver();
        bluemanager = this.registerReceiver(mReceiver, filter);
        buttonstatus = (Button) findViewById(R.id.buttonstatus);
        buttonstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(btAdapter.isEnabled()){
                    text1.setText("BlueTooth Is enabled.");
                }
                else{
                    text1.setText("BlueTooth Is Disabled.");
                }
            }
        });
        buttondiscover = (Button) findViewById(R.id.buttondiscover);
        buttondiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                System.out.println("discover clicked");
                clearView();
                resultList.clear();
                deviceList.clear();
                discover();
            }
        });
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            text1.setText("BlueTooth not available.");
        }


    }
    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        System.out.println("list clicked "+position+" --id "+id);
        System.out.println("compare-> "+deviceList.get(position)+list1.getItemAtPosition(position).getClass().getName()+" id"+id);
        btAdapter.cancelDiscovery();
        new AlertDialog.Builder(this).setMessage("Connect?").setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                try {
                    Method method = deviceList.get(position).getClass().getMethod("createBond", (Class[]) null);
                    method.invoke(deviceList.get(position), (Object[]) null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setNegativeButton("Cancel", null).show();
        /*try {
            Method method = deviceList.get(position).getClass().getMethod("createBond", (Class[]) null);
            method.invoke(deviceList.get(position), (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void discover(){
        if(btAdapter.startDiscovery()) {
            System.out.println("discovering..");
        }
        else
            text1.setText("Discovery is not successfully");
    }
    public void constructAdapter(BluetoothDevice device){
        resultList.add(device.getName()+"\n"+device.getAddress());
        deviceList.add(device);
        list1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.text_list, R.id.textlist, resultList));
        System.out.println(resultList);
    }

    public void clearView(){
        ArrayList<String> empty = new ArrayList<String>();
        empty.clear();
        list1.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.text_list, empty));
    }
    public class BlueReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            System.out.println(action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //constructAdapter((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                resultList.add(device.getName()+"\n"+device.getAddress());
                deviceList.add(device);
                list1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.text_list, R.id.textlist, resultList));
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                //list1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.text_list, R.id.textlist, resultList));
            }
        }
    }
}
