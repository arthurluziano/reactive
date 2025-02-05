package com.luziano.reactive.controller;

import com.luziano.reactive.model.Task;
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

    @GetMapping
    public Mono<List<Task>> getTasks() {
        return taskService.list();
    }

    @PostMapping
    public Mono<Task> createTask(@RequestBody Task task) {
        return taskService.insert(task);
    }
}
