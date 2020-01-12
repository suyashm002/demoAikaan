package com.example.aikaanapp.models.data;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * BatteryUsage model class.
 */
public class BatteryUsage extends RealmObject {

    @PrimaryKey
    public int id;

    // Timestamp of usage instance
    @Index
    public long timestamp;

    // State of the battery. ie. charging, discharging, etc.
    public String state;

    // Level of the battery (0 - 1.0) translates to percentage
    public float level;

    // If screen is on == 1, off == 0
    public int screenOn;

    // Trigger reason
    public String triggeredBy;

    // Additional battery details, temperature, voltage, mA, etc.
    public BatteryDetails details;
}