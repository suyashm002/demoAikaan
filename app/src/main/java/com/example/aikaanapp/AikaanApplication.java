package com.example.aikaanapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.aikaanapp.managers.sampling.BatteryService;
import com.example.aikaanapp.managers.sampling.DataEstimator;
import com.example.aikaanapp.managers.storage.AikaanDbMigration;
import com.example.aikaanapp.tasks.DeleteSessionsTask;
import com.example.aikaanapp.tasks.DeleteUsagesTask;
import com.example.aikaanapp.util.LogUtils;
import com.example.aikaanapp.util.SettingsUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.aikaanapp.util.LogUtils.logI;
import static com.example.aikaanapp.util.LogUtils.makeLogTag;

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
      //  Realm.init(this);
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(Config.DATABASE_VERSION)
                .migration(new AikaanDbMigration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        logI(TAG, "Estimator new instance");


        Context context = getApplicationContext();
        try {
            aikaanADBagent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            aikaanADBagent2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            aikaanADBagent3();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SettingsUtils.isTosAccepted(context)) {
            // TODO call ADB commands
          /*  aikaanADBagent();
            aikaanADBagent2();
            aikaanADBagent3();*/
            // Start GreenHub Service
            logI(TAG, "startAikaanService() called");
            startAikaanService();

            // Delete old data history
            final int interval = SettingsUtils.fetchDataHistoryInterval(context);
            new DeleteUsagesTask().execute(interval);
            new DeleteSessionsTask().execute(interval);

            if (SettingsUtils.isPowerIndicatorShown(context)) {
               // startStatusBarUpdater();
            }
        }
    }

    private void aikaanADBagent() throws Exception {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("adb shell tar -C /data/  -xf aikaan_agent.tar");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(IOException | InterruptedException e){
            throw new Exception(e);
        }
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("adb shell chmod +x /data/opt/aikaan/bin/*\n");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(IOException | InterruptedException e){
            throw new Exception(e);
        }

    }
    private void aikaanADBagent2() throws Exception {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("adb push ./init.aikaan.rc /etc/init/xxxaikaan.rc");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(IOException | InterruptedException e){
            throw new Exception(e);
        }
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("adb shell chmod 0644 /etc/init/xxxaikaan.rc");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(IOException | InterruptedException e){
            throw new Exception(e);
        }


    }

    private void aikaanADBagent3() throws Exception {
        Process process1 = null,process2 = null;

        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("adb push ./run_agent.sh  /system/bin/run_agent.sh\n");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(IOException | InterruptedException e){
            throw new Exception(e);
        }
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("adb shell chmod +x  /system/bin/run_agent.sh");
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            su.waitFor();
        }catch(IOException | InterruptedException e){
            throw new Exception(e);
        }

    }
    public void startAikaanService() {
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

    public void stopAikaanService() {
        Intent service = new Intent(getApplicationContext(), BatteryService.class);
        stopService(service);
    }

    public DataEstimator getEstimator() {
        return BatteryService.estimator;
    }

   /* public void startStatusBarUpdater() {
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
*/
    public void stopStatusBarUpdater() {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mNotificationIntent);
        }
    }
}
