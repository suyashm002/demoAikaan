package com.example.aikaanapp.models;

import static com.example.aikaanapp.util.LogUtils.makeLogTag;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;
/**
 * Service properties model.
 */
public class Service {

    private static final String TAG = makeLogTag(Service.class);

    private static WeakReference<List<RunningServiceInfo>> runningServiceInfo = null;

    /**
     * Returns a list of currently running Services.
     *
     * @param context the Context.
     * @return Returns a list of currently running Services.
     */
    public static List<RunningServiceInfo> getRunningServiceInfo(Context context) {
        if (runningServiceInfo != null && runningServiceInfo.get() != null) {
            return runningServiceInfo.get();
        }

        ActivityManager manager =
                (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServices = manager.getRunningServices(255);
        runningServiceInfo = new WeakReference<>(runningServices);

        return runningServices;
    }

    public static void clear() {
        runningServiceInfo = null;
    }
}