package com.rumbaapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class Login extends FragmentActivity {

    TextView textView;
    public static Activity finalizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        finalizar=this;
        textView= (TextView) findViewById(R.id.rumba);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"font/Roboto-Italic.ttf");
        textView.setTypeface(typeface);
    }
}
