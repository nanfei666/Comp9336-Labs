package com.example.apple.task_03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView tv;
    private SensorManager mySensormanager;
    private Sensor mySensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = mySensormanager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mySensormanager.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_UI);

        tv=(TextView) findViewById(R.id.textView);
    }

    @Override
    public void onSensorChanged(SensorEvent Event) {
       if(Event.values[1]>= -90 && Event.values[1]<=-40) {
//           tv.setText(String.valueOf(Event.values[1]));
           tv.setText(null);
           tv.setText("Default");
       }
        if(Event.values[1]<= 0 && Event.values[1]>=-10) {
            tv.setText(null);
            tv.setText("On the table");
        }
        if(Event.values[1]>= 0 && Event.values[1]<=90) {
            tv.setText(null);
            tv.setText("Upside Down");
        }
        if(Event.values[2]>=-90 && Event.values[2]<=-60) {
            tv.setText(null);
            tv.setText("Right");
        }
        if(Event.values[2]<=90 && Event.values[2]>=70) {
            tv.setText(null);
            tv.setText("Left");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
