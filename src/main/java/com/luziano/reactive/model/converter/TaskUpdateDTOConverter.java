package com.luziano.reactive.model.converter;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.model.dto.TaskInsertDTO;
import com.luziano.reactive.model.dto.TaskUpdateDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskUpdateDTOConverter {

    public Task convert(TaskUpdateDTO taskUpdateDTO) {
        return Optional.ofNullable(taskUpdateDTO)
                .map(source -> Task.builder()
                        .withTitle(source.getTitle())
                        .withDescription(source.getDescription())
                        .withPriority(source.getPriority())
                        .build())
                .orElse(null);
    }
}
