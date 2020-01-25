package com.example.aikaanapp.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.aikaanapp.events.RefreshEvent;
import com.example.aikaanapp.network.CommunicationManager;
import com.example.aikaanapp.util.SettingsUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * ConnectivityReceiver.
 */
public class ConnectivityReceiver extends BroadcastReceiver {
    /**
     * Used to start update network status.
     *
     * @param context the context
     * @param intent  the intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        int state;
        String action = intent.getAction();

        if (action == null) return;

        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(
                                Context.CONNECTIVITY_SERVICE
                        );

                if (connectivityManager == null) return;

                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (!isConnected) {
                    EventBus.getDefault().post(new RefreshEvent("wifi", false));
                    EventBus.getDefault().post(new RefreshEvent("mobile", false));
                    return;
                }

                // Reset upload uploadAttempts counter on network state change
                CommunicationManager.uploadAttempts = 0;

                // Update Server Status
               // new ServerStatusTask().execute(context);

                if (SettingsUtils.isDeviceRegistered(context)) {
                  //  new CheckNewMessagesTask().execute(context);
                }

                if (CommunicationManager.isQueued && SettingsUtils.isServerUrlPresent(context)) {
                    CommunicationManager manager = new CommunicationManager(context, true);
                    manager.sendSamples();
                    CommunicationManager.isQueued = false;
                }

                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    EventBus.getDefault().post(new RefreshEvent("wifi", true));
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    EventBus.getDefault().post(new RefreshEvent("mobile", true));
                }
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_ON) {
                    EventBus.getDefault().post(new RefreshEvent("bluetooth", true));
                } else {
                    EventBus.getDefault().post(new RefreshEvent("bluetooth", false));
                }
                break;
        }
    }
}