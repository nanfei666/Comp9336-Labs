package com.example.apple.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int NumberOfClick = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ClickCounter = (Button)findViewById(R.id.button);
        ClickCounter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myClick(v);
            }
        });
    }


    public void myClick(View v){
        TextView output = (TextView)findViewById(R.id.textView3);
        output.setText(Integer.toString(++NumberOfClick));
        output.setVisibility(View.VISIBLE);
        output.setTextSize(30);
    }
    }

