package com.example.aikaanapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AikaanApplication extends Application {

    private static final String TAG = makeLogTag(AikaanApplication.class);

    private AlarmManager mAlarmManager;

    private PendingIntent mNotificationIntent;

    @Override
    public void onCreate() {
        super.onCreate();

        // If running debug mode, enable logs
        if (BuildConfig.DEBUG) {
            LogUtils.LOGGING_ENABLED = true;
        }

        logI(TAG, "onCreate() called");



        // Database init
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(Config.DATABASE_VERSION)
                .migration(new GreenHubDbMigration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        logI(TAG, "Estimator new instance");


        Context context = getApplicationContext();

        if (SettingsUtils.isTosAccepted(context)) {
            // Start GreenHub Service
            logI(TAG, "startGreenHubService() called");
            startGreenHubService();

            // Delete old data history
            final int interval = SettingsUtils.fetchDataHistoryInterval(context);
            new DeleteUsagesTask().execute(interval);
            new DeleteSessionsTask().execute(interval);

            if (SettingsUtils.isPowerIndicatorShown(context)) {
                startStatusBarUpdater();
            }
        }
    }

    public void startGreenHubService() {
        if (!BatteryService.isServiceRunning) {
            logI(TAG, "GreenHubService starting...");

            final Context context = getApplicationContext();

            new Thread() {

                public void run() {
                    try {
                        Intent service = new Intent(context, BatteryService.class);
                        context.startService(service);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        } else {
            logI(TAG, "GreenHubService is already running...");
        }
    }

    public void stopGreenHubService() {
        Intent service = new Intent(getApplicationContext(), BatteryService.class);
        stopService(service);
    }

    public DataEstimator getEstimator() {
        return BatteryService.estimator;
    }

    public void startStatusBarUpdater() {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        mNotificationIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + Config.REFRESH_STATUS_BAR_INTERVAL,
                Config.REFRESH_STATUS_BAR_INTERVAL,
                mNotificationIntent
        );

    }

    public void stopStatusBarUpdater() {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mNotificationIntent);
        }
    }
}
