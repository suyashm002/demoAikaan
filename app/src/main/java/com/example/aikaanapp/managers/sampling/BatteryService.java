package com.example.aikaanapp.managers.sampling;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.aikaanapp.network.CommunicationManager;
import com.example.aikaanapp.util.SettingsUtils;

import java.io.DataOutputStream;
import java.io.IOException;

import static com.example.aikaanapp.util.LogUtils.logE;
import static com.example.aikaanapp.util.LogUtils.makeLogTag;

public class BatteryService extends Service {

    private static final String TAG = makeLogTag(BatteryService.class);
    public static boolean isServiceRunning = false;

    private IntentFilter mIntentFilter;

    public static DataEstimator estimator = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isServiceRunning = true;

        estimator = new DataEstimator();

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        Context context = getApplicationContext();

        registerReceiver(estimator, mIntentFilter);




        if (SettingsUtils.isSamplingScreenOn(context)) {
            mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
            registerReceiver(estimator, mIntentFilter);
            mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(estimator, mIntentFilter);
        }
       /* if(SettingsUtils.isTosAccepted(context)) {
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
        }*/
    }
    private void aikaanADBagent() throws Exception {
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

            outputStream.writeBytes("adb shell tar -C /data/  -xf aikaan_agent.tar");
            outputStream.flush();

            makeLogTag("Running service");
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
    @Override
    public void onDestroy() {
        isServiceRunning = false;
        try {
            unregisterReceiver(estimator);
        } catch (IllegalArgumentException e) {
            logE(TAG, "Estimator receiver is not registered!");
            e.printStackTrace();
        }
        estimator = null;
    }

}