package com.example.gear.rppm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gear.rppm.R;

public class LoadAppActivity extends AppCompatActivity {

    private Button load_app_button;
    private boolean isClickButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_app);

        load_app_button = (Button) findViewById(R.id.load_app_button1);
        load_app_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickButton = true;
            }
        });

        while (isClickButton){
            Intent i = new Intent(getApplicationContext() ,MainActivity.class);
            startActivity(i);
            finish();
        }


    }
}
