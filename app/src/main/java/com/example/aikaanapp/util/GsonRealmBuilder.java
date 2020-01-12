package com.example.aikaanapp.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;

/**
 * GsonRealmBuilder.
 */
public class GsonRealmBuilder {

    private GsonRealmBuilder() {}

    private static GsonBuilder getBuilder() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                });
    }

    public static Gson get() {
        return getBuilder().create();
    }
}