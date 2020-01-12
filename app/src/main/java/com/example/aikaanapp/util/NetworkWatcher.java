package com.example.aikaanapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkWatcher {

    public static final int BACKGROUND_TASKS = 1;

    public static final int COMMUNICATION_MANAGER = 2;

    /**
     * Checks for Internet connection.
     *
     * @param context Application context
     * @param mode which module is requesting Internet access
     * @return Whether or not it is connected
     */
    public static boolean hasInternet(Context context, int mode) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return activeNetwork.isConnected();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // if communication manager is calling check for settings on mobile data
                if (mode == BACKGROUND_TASKS) {
                    return activeNetwork.isConnected();
                } else if (mode == COMMUNICATION_MANAGER) {
                    return SettingsUtils.isMobileDataAllowed(context) &&
                            activeNetwork.isConnected();
                }
            }
        }
        return false;
    }
}