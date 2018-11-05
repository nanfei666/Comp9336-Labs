package com.example.wangfengyu.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private Button button1;
    private Button buttonchange;
    private TextView text1;
    private TextView textbig;
    private TextView textsmall;
    public float[] gravity = new float[3];
    public float[] filterG = new float[3];
    final float alpha = (float) 0.8;
    String flag = "Q1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        text1 = (TextView) findViewById(R.id.text1);
        textbig = (TextView) findViewById(R.id.textbig);
        textsmall = (TextView) findViewById(R.id.textsmall);
        listview = (ListView) findViewById(android.R.id.list);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                text1.setVisibility(View.VISIBLE);
                listview.setVisibility(View.VISIBLE);
                task1();
            }});
        buttonchange = (Button) findViewById(R.id.button2);
        buttonchange.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //???System.out.println(buttonchange.getText().length() +" - "+ buttonchange.getText().getClass().getSimpleName() );

                if(buttonchange.getText().toString().contains("Q1")){
                    System.out.println("T1 pressed.");
                    flag = "Q2";
                    buttonchange.setText(flag);
                    button1.setVisibility(View.INVISIBLE);
                    listview.setVisibility(View.INVISIBLE);
                    //task2();
                }
                else if (buttonchange.getText().toString() == "Q2"){
                    System.out.println("T2 pressed.");
                    flag = "Q3";
                    buttonchange.setText(flag);
                    text1.setVisibility(View.INVISIBLE);
                    textsmall.setVisibility(View.VISIBLE);
                    textbig.setVisibility(View.VISIBLE);
                    //task3();
                }
                else if (buttonchange.getText() == "Q3"){
                    System.out.println("T3 pressed.");
                    flag = "Q1";
                    buttonchange.setText(flag);
                    textsmall.setVisibility(View.INVISIBLE);
                    textbig.setVisibility(View.INVISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.VISIBLE);
                    text1.setText("Available sensors on this phone");
                    //listview.setVisibility(View.VISIBLE);
                    //task1();
                }
                else
                    System.out.println("buttonchange ERROR!!");
            }});
        //task1();
    }


    public void task1(){
        List< Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<String> showlist = new ArrayList<String>();
        int i = 0;
        for (i =0;i<sensorList.size();i++){
            mSensor = mSensorManager.getDefaultSensor(sensorList.get(i).getType());
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
            showlist.add((i+1)+") "+sensorList.get(i).toString());
        }
        listview.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.text_list, R.id.textlist, showlist));
    }
    /**
     * http://developer.android.com/intl/zh-cn/reference/android/hardware/SensorEvent.html
     * @param x
     * @param y
     * @param z
     */
    public void task2(float x, float y, float z, double filterX, double filterY, double filterZ){
        float gravity = (float) 9.798;
        List< Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        //System.out.println(sensorList.size());
        for (int i = 0;i<sensorList.size();i++){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
            String text1th = "Acceleration force including gravity\nX: "+x+"\nY: "+y+"\nZ: "+z;
            String text2nd = "\nAcceleration force without gravity\nX: "+filterX+"\nY: "+filterY+"\nZ: "+filterZ;
            text1.setText(text1th+text2nd);
        }
    }
    public void task3(float x, float y, float z){
        float threshold = (float) 8.5;
        if(x > threshold){
            textbig.setText("Left");
        }
        else if (x < threshold*-1){
            textbig.setText("Right");
        }
        else if (y > threshold){
            textbig.setText("Default");
        }
        else if (y < threshold*-1){
            textbig.setText("Upside Down");
        }
        else if (z > threshold){
            textbig.setText("On the table");
        }
        else
            textbig.setText(" not stable\n");
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
        return;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			/*gravity[0] = event.values[0];
			gravity[1] = event.values[1];
			gravity[2] = event.values[2];*/

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            filterG[0] = event.values[0] - gravity[0];
            filterG[1] = event.values[1] - gravity[1];
            filterG[2] = event.values[2] - gravity[2];

            if(flag == "Q2"){
                //could have created a new thread to slow down the update rate
                //task2(gravity[0],gravity[1],gravity[2],filterG[0],filterG[1],filterG[2]);
                task2(gravity[0],gravity[1],gravity[2],filterG[0],filterG[1],filterG[2]);
            }
            if(flag == "Q3"){
                task3(gravity[0],gravity[1],gravity[2]);
            }
        }
        return;
    }
}