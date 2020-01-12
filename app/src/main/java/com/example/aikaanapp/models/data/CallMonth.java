package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * Call Month data definition.
 */
public class CallMonth extends RealmObject {

    public int totalCallInNum = 0;

    public int totalCallOutNum = 0;

    public int totalMissedCallNum = 0;

    public long totalCallInDur = 0;

    public long totalCallOutDur = 0;
}