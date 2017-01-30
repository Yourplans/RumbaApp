package com.rumbaapp.activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rumbaapp.R;
import com.rumbaapp.login.Login;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Bienvenida extends AppCompatActivity {

    Timer timer;
    TimerTask timerTask;
    File codigoUsuario = new File("/data/data/com.rumbaapp/files/codigo.txt");
    public static String ip = "http://192.168.0.5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (!codigoUsuario.exists()) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };
        timer.schedule(timerTask, 4000);
    }
}