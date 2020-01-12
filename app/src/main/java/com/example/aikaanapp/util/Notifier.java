package com.example.aikaanapp.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.example.aikaanapp.Config;
import com.example.aikaanapp.MainActivity;
import com.example.aikaanapp.R;

import androidx.core.app.NotificationCompat;

/**
 * Notifier
 */
public class Notifier {

    private static final String TAG = LogUtils.makeLogTag(Notifier.class);

    private static boolean isStatusBarShown = false;

    private static NotificationCompat.Builder sBuilder = null;

    private static NotificationManager sNotificationManager = null;

    public static void startStatusBar(final Context context) {
        if (isStatusBarShown) return;

        // At this moment Inspector still doesn't have a current level assigned
        DataEstimator estimator = new DataEstimator();
        estimator.getCurrentStatus(context);

        double now = Battery.getBatteryCurrentNow(context);
        int level = estimator.getLevel();
        String title = context.getString(R.string.now) + ": " + now + " mA";
        String text = context.getString(R.string.notif_batteryhub_running);

        sBuilder = new NotificationCompat.Builder(context, "start_status")
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(false)
                .setOngoing(true)
                .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        if (level < 100) {
            sBuilder.setSmallIcon(R.drawable.ic_stat_00_pct_charged + level);
        } else {
            sBuilder.setSmallIcon(R.drawable.ic_stat_z100_pct_charged);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        sBuilder.setContentIntent(resultPendingIntent);
        sNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        sNotificationManager.notify(Config.NOTIFICATION_BATTERY_STATUS, sBuilder.build());
        isStatusBarShown = true;
    }

    public static void updateStatusBar(final Context context) {
        // In case status bar is not started yet call start method
        if (!isStatusBarShown) {
            startStatusBar(context);
            return;
        }

        double now = Battery.getBatteryCurrentNow(context);
        int level = (int) (Inspector.getCurrentBatteryLevel() * 100);
        String title = context.getString(R.string.now) + ": " + now + " mA";
        String text = context.getString(R.string.notif_batteryhub_running);

        sBuilder.setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(false)
                .setOngoing(true)
                .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        if (level < 100) {
            sBuilder.setSmallIcon(R.drawable.ic_stat_00_pct_charged + level);
        } else {
            sBuilder.setSmallIcon(R.drawable.ic_stat_z100_pct_charged);
        }

        LogUtils.logI(TAG, "Updating value of notification");

        // Because the ID remains unchanged, the existing notification is updated.
        sNotificationManager.notify(Config.NOTIFICATION_BATTERY_STATUS, sBuilder.build());
    }

    public static void closeStatusBar() {
        sNotificationManager.cancel(Config.NOTIFICATION_BATTERY_STATUS);
        isStatusBarShown = false;
    }

    public static void newMessageAlert(final Context context) {
        if (sNotificationManager == null) {
            sNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "new_message")
                .setSmallIcon(R.drawable.ic_email_white_24dp)
                .setContentTitle(context.getString(R.string.notif_new_message))
                .setContentText(context.getString(R.string.notif_open_inbox))
                .setAutoCancel(true)
                .setOngoing(false)
                .setLights(Color.GREEN, 500, 2000)
                .setVibrate(new long[]{0, 800, 1500})
                .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, InboxActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(InboxActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Because the ID remains unchanged, the existing notification is updated.
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        sNotificationManager.notify(Config.NOTIFICATION_MESSAGE_NEW, notification);
    }

    public static void batteryFullAlert(final Context context) {
        if (sNotificationManager == null) {
            sNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "battery_full")
                        .setSmallIcon(R.drawable.ic_information_white_24dp)
                        .setContentTitle(context.getString(R.string.notif_battery_full))
                        .setContentText(context.getString(R.string.notif_remove_charger))
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setLights(Color.GREEN, 500, 2000)
                        .setVibrate(new long[]{0, 400, 1000})
                        .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(InboxActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Because the ID remains unchanged, the existing notification is updated.
        sNotificationManager.notify(Config.NOTIFICATION_BATTERY_FULL, mBuilder.build());
    }

    public static void batteryLowAlert(final Context context) {
        if (sNotificationManager == null) {
            sNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "battery_low")
                        .setSmallIcon(R.drawable.ic_alert_circle_white_24dp)
                        .setContentTitle(context.getString(R.string.notif_battery_low))
                        .setContentText(context.getString(R.string.notif_connect_power))
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setLights(Color.RED, 500, 2000)
                        .setVibrate(new long[]{0, 400, 1000})
                        .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(InboxActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Because the ID remains unchanged, the existing notification is updated.
        sNotificationManager.notify(Config.NOTIFICATION_BATTERY_LOW, mBuilder.build());
    }

    public static void batteryWarningTemperature(final Context context) {
        if (sNotificationManager == null) {
            sNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "battery_warning")
                        .setSmallIcon(R.drawable.ic_alert_circle_white_24dp)
                        .setContentTitle(context.getString(R.string.notif_battery_warning))
                        .setContentText(context.getString(R.string.notif_battery_warm))
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setLights(Color.YELLOW, 500, 2000)
                        .setVibrate(new long[]{0, 400, 1000})
                        .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("tab", 2);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(InboxActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Because the ID remains unchanged, the existing notification is updated.
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        sNotificationManager.notify(Config.NOTIFICATION_TEMPERATURE_WARNING, notification);
    }

    public static void batteryHighTemperature(final Context context) {
        if (sNotificationManager == null) {
            sNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "battery_high")
                        .setSmallIcon(R.drawable.ic_alert_circle_white_24dp)
                        .setContentTitle(context.getString(R.string.notif_battery_hot))
                        .setContentText(context.getString(R.string.notif_battery_cooldown))
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setLights(Color.RED, 500, 2000)
                        .setVibrate(new long[]{0, 800, 1500})
                        .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("tab", 2);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(InboxActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Because the ID remains unchanged, the existing notification is updated.
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        sNotificationManager.notify(Config.NOTIFICATION_TEMPERATURE_HIGH, notification);
    }

    public static void remainingBatteryTimeAlert(final Context context,
                                                 String timeRemaining, boolean charging) {
        String text = !charging ? "Battery remaining time" : "Remaining time until full charge";

        sBuilder = new NotificationCompat.Builder(context, "remaining_battery_time")
                .setContentTitle(timeRemaining)
                .setContentText(text)
                .setAutoCancel(false)
                .setOngoing(true)
                .setPriority(SettingsUtils.fetchNotificationsPriority(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        if (!charging) {
            sBuilder.setSmallIcon(R.drawable.ic_battery_50_grey600_24dp);
        } else {
            sBuilder.setSmallIcon(R.drawable.ic_battery_50_white_24dp);
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        sBuilder.setContentIntent(resultPendingIntent);
        sNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        sNotificationManager.notify(Config.NOTIFICATION_BATTERY_STATUS, sBuilder.build());
        isStatusBarShown = true;
    }
}