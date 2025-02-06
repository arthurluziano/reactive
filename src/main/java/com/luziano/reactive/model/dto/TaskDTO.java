package com.luziano.reactive.model.dto;

import com.luziano.reactive.model.TaskState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;
}
