package com.luziano.reactive.controller;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.model.converter.TaskDTOConverter;
import com.luziano.reactive.model.dto.TaskDTO;
import com.luziano.reactive.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskDTOConverter converter;

    @GetMapping
    public Mono<List<TaskDTO>> getTasks() {
        return taskService.list()
                .map(converter::convertList);
    }

    @PostMapping
    public Mono<TaskDTO> createTask(@RequestBody Task task) {
        return taskService.insert(task)
                .map(converter::convert);
    }
}
