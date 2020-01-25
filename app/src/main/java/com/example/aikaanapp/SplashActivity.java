package com.example.aikaanapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Starts Main Activity and loading its contents
        startActivity(new Intent(this, MainActivity.class));

        // Finishes the current Splash Activity
        finish();
    }

}