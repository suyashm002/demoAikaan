package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Cell Info data definition.
 */
public class CellInfo extends RealmObject {

    public int mcc = 0;

    public int mnc = 0;

    public int lac = 0;

    public int cid = 0;

    public String radioType = null;
}