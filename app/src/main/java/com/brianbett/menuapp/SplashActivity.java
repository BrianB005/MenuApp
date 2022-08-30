package com.brianbett.menuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("menuReady",null);
        if(token==null){
            startActivity(new Intent(SplashActivity.this,SetUpMenu.class));
        }else{
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
        finish();

    }
}