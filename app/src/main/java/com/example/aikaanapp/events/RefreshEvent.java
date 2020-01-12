package com.example.aikaanapp.events;

public class RefreshEvent {

    public final String field;
    public final boolean value;

    public RefreshEvent(String field, boolean value) {
        this.field = field;
        this.value = value;
    }
}