package com.example.aikaanapp.models;

import android.content.Context;
import android.location.LocationManager;

/**
 * Gps.
 */
public class Gps {

    private static final String TAG = "Gps";

    /**
     * Check whether GPS are enabled.
     *
     * @param context The Context
     * @return whether or not GPS is enabled
     */
    public static boolean isEnabled(final Context context) {
        LocationManager manager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}