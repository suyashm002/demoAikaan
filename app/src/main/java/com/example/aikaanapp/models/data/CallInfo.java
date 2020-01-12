package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * CallInfo data definition.
 */
public class CallInfo extends RealmObject {

    // Incoming call time sum since boot
    public double incomingCallTime;

    // Outgoing call time sum since boot
    public double outgoingCallTime;

    // Non-call time sum since boot
    public double nonCallTime;

    // Idle, offhook or ringing
    public String callStatus;
}