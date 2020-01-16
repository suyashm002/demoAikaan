package com.example.aikaanapp.events;

import com.example.aikaanapp.models.ui.Task;

public class OpenTaskDetailsEvent {
    public final Task task;

    public OpenTaskDetailsEvent(Task task) {
        this.task = task;
    }
}