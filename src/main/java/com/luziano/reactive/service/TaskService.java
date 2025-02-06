package com.luziano.reactive.service;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save);
    }

    public Page<Task> findPaginated(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    private Mono<Task> save(Task task) {
        return Mono.just(task)
                .map(taskRepository::save);
    }

    public Mono<Void> deleteById(String id) {
        return Mono.fromRunnable(() -> taskRepository.deleteById(id));
    }
}
