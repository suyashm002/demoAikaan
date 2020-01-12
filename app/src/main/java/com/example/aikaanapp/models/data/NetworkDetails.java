package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Network Details data definition.
 */
public class NetworkDetails extends RealmObject {

    // wifi, mobile or unknown
    public String networkType;

    // GPRS, EDGE, UMTS, etc.
    public String mobileNetworkType;

    // connecting, connected, disconnected, suspended
    public String mobileDataStatus;

    // none, in, out, inout, dormant
    public String mobileDataActivity;

    // 1 if currently roaming in a foreign mobile network, 0 otherwise
    public int roamingEnabled;

    // disabled, disabling, enabled, enabling, unknown
    public String wifiStatus;

    // As given by getRssi()
    public int wifiSignalStrength;

    // Link speed in Mbps
    public int wifiLinkSpeed;

    // Sent and received data
    public NetworkStatistics networkStatistics;

    // Wifi access point status: disabled, disabling, enabled, enabling, unknown
    public String wifiApStatus;

    // Network infrastructure provider, unbound
    public String networkOperator;

    // Service provider, bound to sim
    public String simOperator;

    // Numeric country code
    public String mcc;

    // Numeric network code
    public String mnc;
}