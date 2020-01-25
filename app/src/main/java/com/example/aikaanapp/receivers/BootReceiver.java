package com.example.aikaanapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.aikaanapp.managers.sampling.BatteryService;
import com.example.aikaanapp.managers.sampling.DataEstimator;
import com.example.aikaanapp.managers.sampling.DataEstimatorService;
import com.example.aikaanapp.util.LogUtils;
import com.example.aikaanapp.util.Notifier;
import com.example.aikaanapp.util.SettingsUtils;

import java.io.DataOutputStream;
import java.io.IOException;

import static com.example.aikaanapp.util.LogUtils.makeLogTag;

/**
 * BootReceiver.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = makeLogTag(BootReceiver.class);

    /**
     * Used to start Service on reboot even when Aikaan is not started.
     *
     * @param context the context
     * @param intent the intent (should be ACTION_BOOT_COMPLETED)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.logI(TAG, "BOOT_COMPLETED onReceive()");
             context.startService(new Intent(context, BatteryService.class));

        if (SettingsUtils.isTosAccepted(context) && SettingsUtils.isPowerIndicatorShown(context)) {

           // Display Status bar
            Notifier.startStatusBar(context);

        }
    }


}