package com.aceturtle.logutilexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aceturtle.logutil.LogDebug;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //LogDebug.isGPSEnabled(MainActivity.this);
        //LogDebug.showToast("Welcome", MainActivity.this);
        //LogDebug.showSettingsDialog(MainActivity.this);

        if(!LogDebug.isGPSEnable(MainActivity.this))
            LogDebug.displayLocationSettingsRequest(MainActivity.this, MainActivity.this);
            //LogDebug.showToast(String.valueOf(LogDebug.isGPSEnabled(MainActivity.this)), MainActivity.this);
        /*else
            LogDebug.showToast(String.valueOf(LogDebug.isGPSEnabled(MainActivity.this)), MainActivity.this);*/
    }
}
