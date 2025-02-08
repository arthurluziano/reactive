package com.luziano.reactive.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateDTO {

    @NotBlank(message = "Value invalid for field \"title\".")
    @Size(min = 3, max = 20, message = "The title must be between 3 and 20 characters.")
    private String title;

    @NotBlank(message = "Value invalid for field \"description\".")
    @Size(max = 200, message = "The description must be less than 200 characters.")
    private String description;

    @Min(value = 1, message = "The priority must be greater than zero.")
    private int priority;
}
