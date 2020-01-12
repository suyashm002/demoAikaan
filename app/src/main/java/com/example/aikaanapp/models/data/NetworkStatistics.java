package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Network Statistics data definition.
 */
public class NetworkStatistics extends RealmObject {

    // Amount of wifi data received
    public double wifiReceived;

    // Amount of wifi data sent
    public double wifiSent;

    // Amount of mobile data received
    public double mobileReceived;

    // Amount of mobile data sent
    public double mobileSent;
}