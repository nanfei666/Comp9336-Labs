package com.example.apple.lab4_sensor_task2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Sensor_02 extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor Liner;
    private TextView TV;
    private TextView TV_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_02);
        mSensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Liner = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        TV = (TextView)findViewById(R.id.with_grvity);
        TV_2= (TextView)findViewById(R.id.without_grivaty);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME);
        // 为加速度传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        // 为方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        // 为陀螺仪传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        // 为磁场传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        // 为重力传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_GAME);
        // 为线性加速度传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME);
        // 为温度传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_GAME);
        // 为光传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME);
        // 为压力传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    public void onSensorChanged(SensorEvent event){

    float[] value = event.values;
    int type = event.sensor.getType();
    StringBuilder sb;
    switch (type) {
        case Sensor.TYPE_ACCELEROMETER:
            sb=new StringBuilder();
            sb.append("Acceleration Force With Gravity"+"\n");
            sb.append("X");
            sb.append(value[0] + "\n");
            sb.append("Y");
            sb.append(value[1] + "\n");
            sb.append("Z");
            sb.append(value[2] + "\n");
            TV.setText(sb.toString());
        break;

        case Sensor.TYPE_LINEAR_ACCELERATION:
            sb=new StringBuilder();
            sb.append("Acceleration Force Without Gravity"+"\n");
            sb.append("X");
            sb.append(value[0] + "\n");
            sb.append("Y");
            sb.append(value[1] + "\n");
            sb.append("Z");
            sb.append(value[2] + "\n");
            TV_2.setText(sb.toString());
        break;

    }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
