package com.example.zhenx.lab8_1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private TextView textview_task1;
    private TextView textview_rotation;
    private Button button_reset;


    private SensorManager mSensorManager;
    private Sensor mSensor;

    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private float angle[] = new float[3];

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // you get nothing, you lose!
    }

    public void onSensorChanged(SensorEvent event)
    {
        if (timestamp != 0)
        {
            final float dT = (event.timestamp - timestamp) * NS2S;

            angle[0] -= event.values[0] * dT;
            angle[1] -= event.values[1] * dT;
            //Log.i("chen_y","y"+angle[1]);
            angle[2] -= event.values[2] * dT;
            //Log.i("chen_z","z"+angle[2]);

            float anglex = (float) Math.toDegrees(angle[0]);
            float angley = (float) Math.toDegrees(angle[1]);
            float anglez = (float) Math.toDegrees(angle[2]);

            textview_task1.setText("X: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
            Log.i("chen_z","z"+event.values[2]);
            String rot = "Roatation: \n" + "X: " + anglex + "\nY: " + angley + "\nZ: " + anglez ;
            textview_rotation.setText(rot);
//            textview_rotation.setText("Rotation Z: " + "\n" +
//                    anglez
//            );
        }
        timestamp = event.timestamp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview_task1 = (TextView) findViewById(R.id.textview_task1);
        textview_rotation = (TextView) findViewById(R.id.textview_rotation);

        button_reset = (Button) findViewById(R.id.button_reset);
        button_reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // reset the angle is to reset the rotation direction.
                angle[0] = 0.0f;
                angle[1] = 0.0f;
                angle[2] = 0.0f;
            }
        });



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
