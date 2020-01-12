package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * CPU Status data definition.
 */
public class CpuStatus extends RealmObject {

    // CPU usage fraction (0-1)
    public double cpuUsage;

    // Uptime in seconds
    public long upTime;

    // Experimental sleep time
    public long sleepTime;
}