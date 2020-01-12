package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Battery Details data definition.
 */
public class BatteryDetails extends RealmObject {

    // Currently ac, usb, or unplugged
    public String charger;

    // Currently Unknown, Unspecified failure, Dead, Cold, Overheat, Over voltage or Good
    public String health;

    // Voltage in Volts
    public double voltage;

    // Temperature in Celsius
    public double temperature;

    // Battery technology
    public String technology;

    // Total capacity in mAh
    public int capacity;

    // Remaining capacity in mAh
    public int remainingCapacity;

    // Battery capacity in microAmpere-hours
    public int chargeCounter;

    // Average battery current in microAmperes
    public int currentAverage;

    // Instantaneous battery current in milliAmperes
    public int currentNow;

    // Battery remaining energy in nanoWatt-hours
    public long energyCounter;

    // age factor ...
}