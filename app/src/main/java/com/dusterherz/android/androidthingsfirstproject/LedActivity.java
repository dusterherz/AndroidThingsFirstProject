package com.dusterherz.android.androidthingsfirstproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.security.PrivateKey;

public class LedActivity extends Activity {
    private static final String TAG = "BlinkActivity";
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;
    private static final String LED_PIN_NAME = "BCM6";

    private Handler mHandler = new Handler();

    private Gpio mLedGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            mLedGpio = service.openGpio(LED_PIN_NAME);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mHandler.post(mBlinkRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        mHandler.removeCallbacks(mBlinkRunnable);
        if (mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }

    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLedGpio == null) {
                return;
            }

            try {
                mLedGpio.setValue(!mLedGpio.getValue());
                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };
}
