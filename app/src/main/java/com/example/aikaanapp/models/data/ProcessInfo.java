package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Process Info data definition.
 */
public class ProcessInfo extends RealmObject {

    // Process Id
    public int processId;

    // Process Name
    public String name;

    // Human readable application name
    public String applicationLabel;

    // If the app is a system app or update to a system app
    public boolean isSystemApp;

    // Foreground, visible, background, service, empty
    public String importance;

    // Version of app, human-readable
    public String versionName;

    // Version of app, android version code
    public int versionCode;

    // Package that installed this process, e.g. com.google.play
    public String installationPkg;

    // Package Permissions
    public RealmList<AppPermission> appPermissions;

    // Signatures of the app from PackageInfo.signatures (it can be empty)
    public RealmList<AppSignature> appSignatures;
}