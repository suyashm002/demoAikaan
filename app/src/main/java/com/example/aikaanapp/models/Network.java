package com.example.aikaanapp.models;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Network.
 */
public class Network {

    private static final String TAG = "Network";

    // Network status constants
    public static final String NETWORKSTATUS_DISCONNECTED = "disconnected";
    public static final String NETWORKSTATUS_DISCONNECTING = "disconnecting";
    public static final String NETWORKSTATUS_CONNECTED = "connected";
    public static final String NETWORKSTATUS_CONNECTING = "connecting";

    // Network type constants
    public static final String TYPE_UNKNOWN = "unknown";

    // Data State constants
    public static String DATA_DISCONNECTED = NETWORKSTATUS_DISCONNECTED;
    public static String DATA_CONNECTING = NETWORKSTATUS_CONNECTING;
    public static String DATA_CONNECTED = NETWORKSTATUS_CONNECTED;
    public static String DATA_SUSPENDED = "suspended";

    // Data Activity constants
    public static String DATA_ACTIVITY_NONE = "none";
    public static String DATA_ACTIVITY_IN = "in";
    public static String DATA_ACTIVITY_OUT = "out";
    public static String DATA_ACTIVITY_INOUT = "inout";
    public static String DATA_ACTIVITY_DORMANT = "dormant";

    // Mobile network constants
    /*
     * we cannot find network types:EVDO_B,LTE,EHRPD,HSPAP from TelephonyManager
     * now
     */
    public static String NETWORK_TYPE_UNKNOWN = "unknown";
    public static String NETWORK_TYPE_GPRS = "gprs";
    public static String NETWORK_TYPE_EDGE = "edge";
    public static String NETWORK_TYPE_UMTS = "utms";
    public static String NETWORK_TYPE_CDMA = "cdma";
    public static String NETWORK_TYPE_EVDO_0 = "evdo_0";
    public static String NETWORK_TYPE_EVDO_A = "evdo_a";
    public static String NETWORK_TYPE_EVDO_B = "evdo_b";
    public static String NETWORK_TYPE_1xRTT = "1xrtt";
    public static String NETWORK_TYPE_HSDPA = "hsdpa";
    public static String NETWORK_TYPE_HSUPA = "hsupa";
    public static String NETWORK_TYPE_HSPA = "hspa";
    public static String NETWORK_TYPE_IDEN = "iden";
    public static String NETWORK_TYPE_LTE = "lte";
    public static String NETWORK_TYPE_EHRPD = "ehrpd";
    public static String NETWORK_TYPE_HSPAP = "hspap";

    private static final int EVDO_B = 12;
    private static final int LTE = 13;
    private static final int EHRPD = 14;
    private static final int HSPAP = 15;

    /**
     * Get the network status, one of connected, disconnected, connecting, or disconnecting.
     *
     * @param context the Context.
     * @return the network status, one of connected, disconnected, connecting, or disconnecting.
     */
    public static String getStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return NETWORKSTATUS_DISCONNECTED;

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) return NETWORKSTATUS_DISCONNECTED;

        NetworkInfo.State state = networkInfo.getState();

        switch (state) {
            case CONNECTED:
                return NETWORKSTATUS_CONNECTED;
            case DISCONNECTED:
                return NETWORKSTATUS_DISCONNECTED;
            case CONNECTING:
                return NETWORKSTATUS_CONNECTING;
            case DISCONNECTING:
                return NETWORKSTATUS_DISCONNECTING;
            default:
                return NETWORKSTATUS_DISCONNECTING;
        }
    }

    /**
     * Get the network type, for example Wifi, mobile, wimax, or none.
     */
    public static String getType(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return TYPE_UNKNOWN;

        NetworkInfo info = manager.getActiveNetworkInfo();

        return (info == null) ? TYPE_UNKNOWN : info.getTypeName();
    }

    /**
     * Returns true if the Internet is reachable.
     *
     * @param context the Context
     * @return true if the Internet is reachable.
     */
    public static boolean isAvailable(final Context context) {
        String network = getStatus(context);
        return network.equals(NETWORKSTATUS_CONNECTED);
    }

    public static boolean isMobileDataEnabled(final Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /* Get network type */
    public static String getMobileNetworkType(Context context) {
        TelephonyManager telManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        int netType = telManager.getNetworkType();

        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return NETWORK_TYPE_1xRTT;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return NETWORK_TYPE_CDMA;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return NETWORK_TYPE_EDGE;
            case EHRPD:
                return NETWORK_TYPE_EHRPD;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return NETWORK_TYPE_EVDO_0;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return NETWORK_TYPE_EVDO_A;
            case EVDO_B:
                return NETWORK_TYPE_EVDO_B;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return NETWORK_TYPE_GPRS;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return NETWORK_TYPE_HSDPA;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return NETWORK_TYPE_HSPA;
            case HSPAP:
                return NETWORK_TYPE_HSPAP;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return NETWORK_TYPE_HSUPA;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_TYPE_IDEN;
            case LTE:
                return NETWORK_TYPE_LTE;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return NETWORK_TYPE_UMTS;
            default:
                // If we don't know the type, just return the number and let the
                // backend take care of it
                return netType + "";
        }
    }

    /* Check is it network roaming */
    public static int getRoamingStatus(Context context) {
        TelephonyManager manager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        return (manager.isNetworkRoaming()) ? 1 : 0;
    }

    /* Get data state */
    public static String getDataState(Context context) {
        TelephonyManager manager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        int dataState = manager.getDataState();

        switch (dataState) {
            case TelephonyManager.DATA_CONNECTED:
                return DATA_CONNECTED;
            case TelephonyManager.DATA_CONNECTING:
                return DATA_CONNECTING;
            case TelephonyManager.DATA_DISCONNECTED:
                return DATA_DISCONNECTED;
            default:
                return DATA_SUSPENDED;
        }
    }

    /* Get data activity */
    public static String getDataActivity(Context context) {
        TelephonyManager manager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        int dataActivity = manager.getDataActivity();
        switch (dataActivity) {
            case TelephonyManager.DATA_ACTIVITY_IN:
                return DATA_ACTIVITY_IN;
            case TelephonyManager.DATA_ACTIVITY_OUT:
                return DATA_ACTIVITY_OUT;
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                return DATA_ACTIVITY_INOUT;
            default:
                return DATA_ACTIVITY_NONE;
        }
    }
}