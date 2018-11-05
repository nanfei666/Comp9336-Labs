package com.example.apple.lab_09_task_1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private Button button_2;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText)findViewById(R.id.editText);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString("setted_text","Default");
        editText.setText(value);



    }
    public void onButtonClick(View v){
        switch (v.getId()) {
            case R.id.button:
            String value = editText.getText().toString();
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("setted_text", value);
            Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
            editor.commit();


        }

    }

}
