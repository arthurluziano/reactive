package com.luziano.reactive.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luziano.reactive.model.Address;
import com.luziano.reactive.model.TaskState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;
    private Address address;
    private LocalDateTime createdAt;
}
