package com.example.aikaanapp.models;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
/**
 * Screen properties model.
 */
public class Screen {
    /**
     * Get Current Screen Brightness Value.
     *
     * @param context The Context
     * @return
     */
    public static int getBrightness(Context context) {
        int screenBrightnessValue = 0;

        try {
            screenBrightnessValue = Settings.System.getInt(
                    context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS
            );
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return screenBrightnessValue;
    }

    /**
     * @param context
     * @return
     */
    public static boolean isAutoBrightness(Context context) {
        boolean autoBrightness = false;

        try {
            int brightnessMode = Settings.System.getInt(
                    context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE
            );
            autoBrightness = (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return autoBrightness;
    }

    /**
     * Get whether the screen is on or off.
     *
     * @return true if the screen is on.
     */
    public static int isOn(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (powerManager == null) return 0;

        if (Build.VERSION.SDK_INT >= 20) {
            return (powerManager.isInteractive()) ? 1 : 0;
        } else {
            //noinspection deprecation
            return (powerManager.isScreenOn()) ? 1 : 0;
        }
    }
}