package com.luziano.reactive.model.converter;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.model.TaskState;
import com.luziano.reactive.model.dto.TaskDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TaskDTOConverter {

    public TaskDTO convert(Task task) {
        return Optional.ofNullable(task)
                .map(source -> {
                    TaskDTO dto = new TaskDTO();

                    dto.setId(source.getId());
                    dto.setTitle(source.getTitle());
                    dto.setDescription(source.getDescription());
                    dto.setPriority(source.getPriority());
                    dto.setState(source.getState());
                    dto.setAddress(source.getAddress());

                    return dto;
                })
                .orElse(null);
    }

    public Task convert(TaskDTO taskDTO) {
        return Optional.ofNullable(taskDTO)
                .map(source -> Task.builder()
                        .withId(source.getId())
                        .withTitle(source.getTitle())
                        .withDescription(source.getDescription())
                        .withPriority(source.getPriority())
                        .withState(source.getState())
                        .build())
                .orElse(null);
    }

    public Task convert(String id, String title, String description, int priority, TaskState state) {
        return Task.builder()
                .withId(id)
                .withTitle(title)
                .withDescription(description)
                .withPriority(priority)
                .withState(state)
                .build();
    }

    public List<TaskDTO> convertList(List<Task> taskList) {
        return Optional.ofNullable(taskList)
                .map(array -> array.stream().map(this::convert).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}
