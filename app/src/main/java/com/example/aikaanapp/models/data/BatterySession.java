package com.example.aikaanapp.models.data;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * BatterySession.
 */
public class BatterySession extends RealmObject {

    @PrimaryKey
    public int id;

    // Timestamp of session
    @Index
    public long timestamp;

    // Level of the battery (0 - 1.0) translates to percentage
    public float level;

    // If screen is on == 1, off == 0
    public int screenOn;

    // Trigger reason
    public String triggeredBy;
}