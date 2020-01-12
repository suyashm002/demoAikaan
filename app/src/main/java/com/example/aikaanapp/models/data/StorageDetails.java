package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Storage Details data definition.
 * Free and total storage space in megabytes
 */
public class StorageDetails extends RealmObject {

    public int free;

    public int total;

    public int freeExternal;

    public int totalExternal;

    public int freeSystem;

    public int totalSystem;

    public int freeSecondary;

    public int totalSecondary;
}