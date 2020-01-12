package com.example.aikaanapp.models.data;

import io.realm.RealmObject;

public class AppPermission extends RealmObject {

    public String permission;

    public AppPermission() {}

    public AppPermission(String permission) {
        this.permission = permission;
    }
}