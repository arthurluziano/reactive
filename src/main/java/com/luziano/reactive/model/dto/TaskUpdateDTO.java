package com.luziano.reactive.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {

    @NotBlank(message = "{blank.title}")
    @Size(min = 3, max = 20, message = "{invalid.title}")
    private String title;

    @NotBlank(message = "{blank.description}")
    @Size(max = 200, message = "{invalid.description}")
    private String description;

    @Min(value = 1, message = "{invalid.priority}")
    private int priority;
}
