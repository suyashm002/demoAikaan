package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * AppSignature.
 */
public class AppSignature extends RealmObject {

    public String signature;

    public AppSignature() {}

    public AppSignature(String signature) {
        this.signature = signature;
    }
}