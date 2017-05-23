package com.dusterherz.android.androidthingsfirstproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class ButtonActivity extends Activity {
    private static final String TAG = "ButtonActivity";
    private static final String BUTTON_PIN_NAME = "BCM21";

    private Gpio mButtonGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService service = new PeripheralManagerService();

        try {
            mButtonGpio = service.openGpio(BUTTON_PIN_NAME);
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mButtonGpio.registerGpioCallback(mCallback);
        } catch (IOException e) {
            Log.e(TAG, "Error on peripheralIO API", e);
        }
    }

    private GpioCallback mCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            Log.i(TAG, "GPIO changed, button pressed");
            Intent intent = new Intent(ButtonActivity.this, LedActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mButtonGpio != null) {
            mButtonGpio.unregisterGpioCallback(mCallback);
            try {
                mButtonGpio.close();
            } catch (IOException e) {
                Log.e (TAG, "Error on PeriphericalIO API", e);
            }
        }
    }
}
