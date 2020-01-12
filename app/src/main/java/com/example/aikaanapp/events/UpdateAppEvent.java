package com.example.aikaanapp.events;

public class UpdateAppEvent {

    public final int version;

    public UpdateAppEvent(int version) {
        this.version = version;
    }
}