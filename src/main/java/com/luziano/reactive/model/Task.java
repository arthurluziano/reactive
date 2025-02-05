package com.luziano.reactive.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private String title;
    private String description;
    private int priority;
    private TaskState state;
}
