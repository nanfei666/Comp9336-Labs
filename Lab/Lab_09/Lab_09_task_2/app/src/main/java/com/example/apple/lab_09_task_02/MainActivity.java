package com.example.apple.lab_09_task_02;

import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=(TextView)findViewById(R.id.textView);
        button= (Button)findViewById(R.id.button);






    }

    public static long getTotalExternalMemorySize(){
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return (totalBlocks * blockSize)/1048576;
    }

    public void onButtonClick(View v){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            tv.setText("");
            tv.append("external storage is available for read and write");
            tv.append("Capacity is"+ getTotalExternalMemorySize());


        }
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            tv.setText("");
            tv.setText("external storage is available to at least read");
            tv.append("\nCapacity is "+ getTotalExternalMemorySize()+" Gigs");

        }
    }
}
