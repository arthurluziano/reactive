package com.luziano.reactive.exception;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException() {
        super("Task not found.");
    }
}
