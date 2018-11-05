package com.example.apple.lab_06_task_1;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Intent batteryStatus;
    private TextView tv;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = this.registerReceiver(null,ifilter);
        tv=(TextView)findViewById(R.id.textView);
        tv.setGravity(Gravity.CENTER);
        btn = (Button)findViewById(R.id.button);

    }
    public void onButtonClick(View v){
        StringBuffer sb = new StringBuffer();
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        String batteryPct = String.valueOf(level / (float) scale);
        sb.append("Current level of battery is :"+batteryPct);
        if(usbCharge){
            sb.append("\nMobile is charging via usb");
        }
        if(acCharge){
            sb.append("\nMobile is charging via ac");
        }
        tv.setText(sb);

    }
}
