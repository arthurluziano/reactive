package com.luziano.reactive.service;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save);
    }

    public Mono<List<Task>> list() {
        return Mono.just(taskRepository.findAll());
    }

    private Mono<Task> save(Task task) {
        return Mono.just(task)
                .map(taskRepository::save);
    }
}
