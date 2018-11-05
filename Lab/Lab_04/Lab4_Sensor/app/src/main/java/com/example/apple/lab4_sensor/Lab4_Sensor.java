package com.example.apple.lab4_sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class Lab4_Sensor extends AppCompatActivity implements SensorEventListener {
    //private TextView tv;
    //private TextView withou_grivate;
    private ListView lv;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    //private TextView mTxtValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4__sensor);
        //withou_grivate = (TextView) findViewById(R.id.without_gravity);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        /*
        float[] values = event.values;
        StringBuilder sb = new StringBuilder();
        sb.append("X：");
        sb.append(values[0]);
        sb.append("\nY：");
        sb.append(values[1]);
        sb.append("\nZ：");
        sb.append(values[2]);
        withou_grivate.setText(sb.toString());*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onClck(View V) {

    lv = (ListView) findViewById(R.id.LV);
    SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
    String[] msg=new String[sensors.size()];
    final ArrayList<String> list = new ArrayList<String>();
    int index = 0;

    for (Sensor sen : sensors) {
        list.add("Type:" + sen.getType() + "\n"+"Name:" + sen.getName() + "\n"+"Version:" + sen.getVersion() + "\n"+"Vendor:" + sen.getVendor() + "\n");

    }

    ArrayAdapter<String> array =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
    lv.setAdapter(array);


    }
}
