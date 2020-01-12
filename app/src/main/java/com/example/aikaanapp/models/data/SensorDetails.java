package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Sensor Details data definition.
 */
public class SensorDetails extends RealmObject {

    public int fifoMaxEventCount;

    public int fifoReservedEventCount;

    //Get the highest supported direct report mode rate level of the sensor.
    public int highestDirectReportRateLevel;

    public int id;
    //This value is defined only for continuous and on-change sensors.
    public int maxDelay;

    public float maximumRange;

    public int minDelay;

    public String name;

    //the power in mA used by this sensor while in use
    public float power;

    //Each sensor has exactly one reporting mode associated with it.
    public int reportingMode;

    public float resolution;

    public String stringType;

    public int codeType;

    public String vendor;

    public int version;

    //Returns true if the sensor supports sensor additional information API
    public boolean isAdditionalInfoSupported;

    //Returns true if the sensor is a dynamic sensor.
    public boolean isDynamicSensor;

    //https://developer.android.com/reference/android/hardware/Sensor.html#isWakeUpSensor()
    public boolean isWakeUpSensor;

    //Number of sensor events in a given time interval
    public int frequencyOfUse = 0;

    //start sample usage Count
    public long iniTimestamp = 0;

    //end of sample usage count
    public long endTimestamp = 0;
}