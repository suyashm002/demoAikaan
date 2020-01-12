package com.example.aikaanapp.models;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.example.aikaanapp.R;

/**
 * Bluetooth.
 */
public class Bluetooth {
    /**
     * Checks if bluetooth is enabled on the device
     *
     * @return true if bluetooth is enabled
     */
    public static boolean isEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    @SuppressLint("HardwareIds")
    public static String getAddress(final Context context) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return (adapter != null) ? adapter.getAddress() : context.getString(R.string.not_available);
    }
}