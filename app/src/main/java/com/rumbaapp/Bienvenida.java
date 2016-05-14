package com.rumbaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Bienvenida extends AppCompatActivity {

    Timer timer;
    TimerTask timerTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {

                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();

            }
        };timer.schedule(timerTask,4000);

    }
}
