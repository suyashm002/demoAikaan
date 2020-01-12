package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Device data definition.
 */
public class Device extends RealmObject {

    public String uuId;

    public String model;

    public String manufacturer;

    public String brand;

    public String product;

    public String osVersion;

    public String kernelVersion;

    public int isRoot;
}