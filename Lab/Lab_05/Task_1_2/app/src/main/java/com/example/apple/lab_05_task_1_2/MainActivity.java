package com.example.apple.lab_05_task_1_2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mySensormanage;
    private Sensor mSensor;
    private float timestamp;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float positionY;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySensormanage = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mySensormanage.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mySensormanage.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_UI);

        tv=(TextView)findViewById(R.id.textView);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()== Sensor.TYPE_GYROSCOPE){

            if(timestamp !=0){
                final float dT=(sensorEvent.timestamp-timestamp)*NS2S;
                positionY = positionY+sensorEvent.values[2] * dT;
                float angleY = (float)Math.toDegrees(positionY);
                tv.setText("Rotation :" + String.valueOf(angleY));

            }
            else {
                tv.setText("Rotation :" );
            }
            timestamp = sensorEvent.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
//    @Override
//    protected void onResume(){
//        super.onResume();
//        mySensormanage.registerListener(this,mySensormanage.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_FASTEST);
//    }
}
