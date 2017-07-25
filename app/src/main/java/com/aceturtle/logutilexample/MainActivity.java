package com.aceturtle.logutilexample;

import android.app.Activity;
import android.content.Intent;
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
        else
            LogDebug.createGeofence(MainActivity.this);
            //LogDebug.showToast(String.valueOf(LogDebug.isGPSEnabled(MainActivity.this)), MainActivity.this);
        /*else
            LogDebug.showToast(String.valueOf(LogDebug.isGPSEnabled(MainActivity.this)), MainActivity.this);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case LogDebug.REQUEST_CHECK_SETTINGS :
                switch (resultCode){
                    case Activity.RESULT_OK :
                     LogDebug.createGeofence(MainActivity.this);
                        break;
                    case Activity.RESULT_CANCELED :
                        finish();
                        //Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }
}
