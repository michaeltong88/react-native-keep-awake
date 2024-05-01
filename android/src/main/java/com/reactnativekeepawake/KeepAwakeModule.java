package com.reactnativekeepawake;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = KeepAwakeModule.NAME)
public class KeepAwakeModule extends ReactContextBaseJavaModule {
    public static final String NAME = "KeepAwake";

    private static ReactApplicationContext reactContext;

    private PowerManager.WakeLock wakeLockForScreenOn = null;
    private PowerManager.WakeLock wakeLockForWakeUp = null;

    public KeepAwakeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void setKeepScreenOn(final boolean lock) {
        final Activity activity = getCurrentActivity();

        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lock) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    } else if (wakeLockForScreenOn == null) {
                        PowerManager powerManager = (PowerManager) reactContext.getSystemService(reactContext.POWER_SERVICE);
                        wakeLockForScreenOn = powerManager.newWakeLock(
                          PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
                          "HealthJay::WakeLock_ScreenOn"
                        );

                        //acquire will turn on the display
                        wakeLockForScreenOn.acquire();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    } else if (wakeLockForScreenOn != null) {
                        wakeLockForScreenOn.release();
                        wakeLockForScreenOn = null;
                    }
                }
            }
        });
    }

    @ReactMethod
    public void setShowWhenLocked(final boolean lock) {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    activity.setShowWhenLocked(lock);
                } else {
                    if (lock) {
                        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                    } else {
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                    }
                }
            }
        });
    }

    @ReactMethod
    public void setSystemUiVisibility(final boolean lock) {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View decorView = activity.getWindow().getDecorView();

                int newUiOptions = decorView.getSystemUiVisibility();

                if (lock) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                    newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    // Hide the nav bar and status bar
                    newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;

                    decorView.setSystemUiVisibility(newUiOptions);
                } else {
                    newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                    newUiOptions ^= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    // Hide the nav bar and status bar
                    newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(newUiOptions);
                }
            }
        });
    }

    @ReactMethod
    public void wakeScreen(final boolean wakeup) {
        PowerManager powerManager = (PowerManager) getReactApplicationContext().getSystemService(getReactApplicationContext().POWER_SERVICE);
        boolean isScreenOn = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH ? powerManager.isInteractive() : powerManager.isScreenOn();

        if (wakeup && !isScreenOn) {
            wakeLockForWakeUp = powerManager.newWakeLock(
              PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
              "HealthJay:WakeLock_WakeUp"
            );

            if (wakeLockForWakeUp != null) {
                wakeLockForWakeUp.acquire();
            }
        } else if (!wakeup && wakeLockForWakeUp != null) {
            wakeLockForWakeUp.release();
            wakeLockForWakeUp = null;
        }
    }
}
