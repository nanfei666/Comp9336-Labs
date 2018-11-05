package com.example.apple.task_3_12;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;

import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;

public class Gps_Reader extends AppCompatActivity {

    LocationManager locationManager;
    Location location;
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps__reader);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final TextView GPS_status = (TextView) findViewById(R.id.GPS_status);
        final TextView locationInfo = (TextView) findViewById(R.id.locationInfo);
        Button btnGetGpsStatus = (Button) findViewById(R.id.btnGpsStatus);
        final Button btnGetLocation = (Button) findViewById(R.id.btnLocation);


        btnGetGpsStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GpsStatus()) {
                    GPS_status.setText("GPS is active.");
                    btnGetLocation.setVisibility(View.VISIBLE);
                    btnGetLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String txt = GetLocation();
                            locationInfo.setText(txt);
                        }
                    });
                } else
                    showAlertDialog();
            }
        });
    }


    public String GetLocation() {
        Toast.makeText(getApplicationContext(), "Fetching location details, please wait a minute.", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Double altitude = 0.0;
        Double longitute = 0.0;
        Double latitude = 0.0;
        long time = 0;
        String provider = "";
        float speed = 0f;
        float accuracy = 0f;

        if (!isGPSEnabled && !isNetworkEnabled) {
            return "can not get your location.";
        } else {
            if (isNetworkEnabled) {
                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, listener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location != null) {
                        altitude = location.getAltitude();
                        latitude = location.getLatitude();
                        longitute = location.getLongitude();
                        time = location.getTime();
                        provider = location.getProvider();
                        speed = location.getSpeed();
                        accuracy = location.getAccuracy();
                    }else{
                        return "Error 001: location==null";
                    }
                }else{
                    return "Error 002: locationManager==null";
                }
            }else{
                //GPSEnabled == true
                if(location==null){
                    if(locationManager!=null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5,  listener);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(location!=null){
                            altitude = location.getAltitude();
                            latitude = location.getLatitude();
                            longitute = location.getLongitude();
                            time = location.getTime();
                            provider = location.getProvider();
                            speed = location.getSpeed();
                            accuracy = location.getAccuracy();
                        }else{
                            return "Error 004: location == null";
                        }
                    }else{
                        return "Error 003: locationmanager == null";
                    }
                }
            }
        }
        Timestamp timestamp = new Timestamp(time);
        return "Date/Time: "+timestamp+"\nProvider: "+provider+"\nAccuracy: "+accuracy+"\nLatitude: "+latitude+"\nLongtitude: "
                +longitute+"\nAltitude: "+altitude+"\nspeed: "+speed;

    }


    public boolean GpsStatus(){
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps_status;
    }


    public void showAlertDialog(){
//        String mss = "GPS is not enabled. Do you want to go to settings menu?";
        String mss = "GPS is not enabled. Please turn on the GPS service and try again.";
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Setting the GPS");
        mBuilder.setMessage(mss);

        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.create().show();
    }

}