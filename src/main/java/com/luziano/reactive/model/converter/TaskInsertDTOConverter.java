package com.luziano.reactive.model.converter;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.model.dto.TaskInsertDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskInsertDTOConverter {

    public Task convert(TaskInsertDTO taskInsertDTO) {
        return Optional.ofNullable(taskInsertDTO)
                .map(source -> Task.builder()
                        .withTitle(source.getTitle())
                        .withDescription(source.getDescription())
                        .withPriority(source.getPriority())
                        .build())
                .orElse(null);
    }
}
