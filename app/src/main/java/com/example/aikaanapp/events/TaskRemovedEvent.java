package com.example.aikaanapp.events;

public class TaskRemovedEvent {

    public final int position;

    public final Task task;

    public TaskRemovedEvent(int position, Task task) {
        this.position = position;
        this.task = task;
    }
}