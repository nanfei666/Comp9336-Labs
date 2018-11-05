package com.example.apple.lab_05_task_3_4_5;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView textview_task3;

    private TextView textview_magnetometer;
    private TextView textview_groundtruth;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Sensor oSensor;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            textview_task3.setText("X: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        } else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            textview_magnetometer.setText("Exact heading: \n" + event.values[0]);


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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

                textview_groundtruth.setText("true north: \n" + (event.values[0] - geoField.getDeclination()));
            }else{
                textview_groundtruth.setText("Location null..");
            }


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
