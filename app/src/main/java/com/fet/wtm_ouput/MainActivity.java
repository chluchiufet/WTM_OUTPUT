package com.fet.wtm_ouput;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {

    private Gpio mRedLed;
    private Handler mHandler = new Handler();
    private static final String REDLED = "BCM19";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeripheralManager service = PeripheralManager.getInstance();

        try {
            mRedLed = service.openGpio(REDLED);
            mRedLed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mRedLed.setValue(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mHandler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mRedLed != null) {
            try {
                mRedLed.close();
                mRedLed = null;
            } catch (IOException e) {
                Log.w("WTM", "Unable to close GPIO", e);
            }
        }
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                if(mRedLed.getValue()){
                    try {
                        mRedLed.setValue(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        mRedLed.setValue(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHandler.postDelayed(runnable,1000);
        }
    };


}
