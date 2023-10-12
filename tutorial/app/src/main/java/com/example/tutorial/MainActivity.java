package com.example.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private CheckBox tasnia,shahrin,orin;
    private RadioGroup radiogrp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasnia=findViewById(R.id.checkTasnia);
        shahrin=findViewById(R.id.checkTasnia);
        orin=findViewById(R.id.checkTasnia);
        radiogrp=findViewById(R.id.radiogrp);
        progressBar=findViewById(R.id.progressbar);
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++) {
                    progressBar.incrementProgressBy(10);
                    SystemClock.sleep(500);
                }
            }

        });
        thread.start();
        radiogrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int id=checkedId;
                if(id==R.id.radioMarried){
                    Toast.makeText(MainActivity.this, "married", Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.radioUnmarried){
                    Toast.makeText(MainActivity.this, "unmarried", Toast.LENGTH_SHORT).show();

                }
            }
        });
        if(tasnia.isChecked()){
            Toast.makeText(MainActivity.this, "you are tasnia", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this, "you are not tasnia", Toast.LENGTH_SHORT).show();
        }
        tasnia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity.this, "you are tasnia", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "you are not tasnia", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}