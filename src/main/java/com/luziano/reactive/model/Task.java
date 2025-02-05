package com.luziano.reactive.model;

import com.luziano.reactive.service.TaskService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private String title;
    private String description;
    private int priority;
    private TaskState state;

    public Task newTask() {
        TaskService.taskList.add(this);
        return this;
    }
}
