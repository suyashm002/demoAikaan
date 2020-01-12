package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * SettingsInfo data definition.
 */
public class Settings extends RealmObject {

    public boolean bluetoothEnabled;

    public boolean locationEnabled;

    public boolean powersaverEnabled;

    public boolean flashlightEnabled;

    public boolean nfcEnabled;

    // Unknown source app installation on == 1, off == 0
    public int unknownSources;

    // Developer mode on == 1, off == 0
    public int developerMode;
}