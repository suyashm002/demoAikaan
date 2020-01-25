package com.example.aikaanapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import com.example.aikaanapp.events.BatteryTimeEvent;
import com.example.aikaanapp.events.PowerSourceEvent;
import com.example.aikaanapp.managers.sampling.Inspector;
import com.example.aikaanapp.managers.storage.AikaanDb;
import com.example.aikaanapp.models.Battery;
import com.example.aikaanapp.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import io.realm.exceptions.RealmMigrationNeededException;

import static com.example.aikaanapp.util.LogUtils.makeLogTag;

public class PowerConnectionReceiver extends BroadcastReceiver {

    private static final String TAG = makeLogTag(PowerConnectionReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) return;

        boolean isCharging = false;
        String batteryCharger = "";
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            isCharging = true;

            final Intent mIntent = context.getApplicationContext()
                    .registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

            if (mIntent == null) return;

            int chargePlug = mIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            boolean wirelessCharge = false;

            if (Build.VERSION.SDK_INT >= 21) {
                wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;
            }

            if (acCharge) {
                batteryCharger = "ac";
                EventBus.getDefault().post(new PowerSourceEvent("ac"));
            } else if (usbCharge) {
                batteryCharger = "usb";
                EventBus.getDefault().post(new PowerSourceEvent("usb"));
            } else if (wirelessCharge) {
                batteryCharger = "wireless";
                EventBus.getDefault().post(new PowerSourceEvent("wireless"));
            }
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            isCharging = false;
            EventBus.getDefault().post(new PowerSourceEvent("unplugged"));
        }
        // Post to subscribers & update notification
        int batteryRemaining =
                (int) (Battery.getRemainingBatteryTime(context, isCharging, batteryCharger) / 60);
        int batteryRemainingHours = batteryRemaining / 60;
        int batteryRemainingMinutes = batteryRemaining % 60;

        EventBus.getDefault().post(
                new BatteryTimeEvent(batteryRemainingHours, batteryRemainingMinutes, isCharging)
        );
//        Notifier.remainingBatteryTimeAlert(
//                context,
//                batteryRemainingHours + "h " + batteryRemainingMinutes + "m", isCharging
//        );

        try {
            // Save a new Battery Session to the mDatabase
            AikaanDb database = new AikaanDb();
            LogUtils.logI(TAG, "Getting new session");
            database.saveSession(Inspector.getBatterySession(context, intent));
            database.close();
        } catch (IllegalStateException | RealmMigrationNeededException e) {
            LogUtils.logE(TAG, "No session was created");
            e.printStackTrace();
        }
    }
}