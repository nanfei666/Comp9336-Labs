package com.example.zhenx.lab8_2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{

    private  TextView textview_task3;

    private TextView textview_magnetometer;
    private TextView textview_groundtruth;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor oSensor;

    private LocationManager locationManager;


    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // you get nothing, you lose!
    }

    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            textview_task3.setText("X: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        }
        else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            textview_magnetometer.setText("Exact heading: \n" + event.values[0]);



            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if(location==null){
                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            }
            if(location!=null) {
                GeomagneticField geoField;
                geoField = new GeomagneticField(
                        Double.valueOf(location.getLatitude()).floatValue(),
                        Double.valueOf(location.getLongitude()).floatValue(),
                        Double.valueOf(location.getAltitude()).floatValue(),
                        System.currentTimeMillis()
                );
                Log.i("CHEN", "declination: " + geoField.getDeclination());

                textview_groundtruth.setText("true north: \n" + (event.values[0] - geoField.getDeclination()));
            }else{
                textview_groundtruth.setText("Location null..");
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textview_task3 = (TextView) findViewById(R.id.textview_task3);

        textview_magnetometer = (TextView) findViewById(R.id.textview_magnetometer);

        textview_groundtruth = (TextView) findViewById(R.id.textview_groundtruth);

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        oSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, oSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, oSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
