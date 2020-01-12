package com.example.aikaanapp.events;

public class OpenTaskDetailsEvent {
    public final Task task;

    public OpenTaskDetailsEvent(Task task) {
        this.task = task;
    }
}