package com.dusterherz.android.androidthingsfirstproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;

public class HomeActivity extends Activity {
    public static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService service = new PeripheralManagerService();
        Log.d(TAG, "Available GPIO: " + service.getGpioList());
        Intent intent = new Intent(this, ButtonActivity.class);
        startActivity(intent);
    }
}
