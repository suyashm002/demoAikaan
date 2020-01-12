package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

/**
 * LocationProvider.
 */
public class LocationProvider extends RealmObject {

    public String provider;

    public LocationProvider() {}

    public LocationProvider(String provider) {
        this.provider = provider;
    }
}