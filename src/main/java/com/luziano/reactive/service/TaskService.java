package com.luziano.reactive.service;

import com.luziano.reactive.exception.TaskNotFoundException;
import com.luziano.reactive.model.Task;
import com.luziano.reactive.repository.TaskCustomRepository;
import com.luziano.reactive.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TaskService {

    private final static Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskCustomRepository taskCustomRepository;

    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save)
                .doOnError(error -> log.error("Error during save task with title \"{}\". Message: {} - ({})", task.getTitle(), error.getMessage(), LocalDateTime.now()));
    }

    public Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        return taskCustomRepository.findPaginated(task, pageNumber, pageSize)
                .doOnNext(it -> log.info("Listing tasks... - ({})", LocalDateTime.now()));
    }

    public Mono<Task> update(String id, Task task) {
        return taskRepository.findById(id)
                .map(task::update)
                .flatMap(taskRepository::save)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> log.error("Error during update task with id \"{}\". Message: {} - ({})", task.getId(), error.getMessage(), LocalDateTime.now()));
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }

    private Mono<Task> save(Task task) {
        return Mono.just(task)
                .doOnNext(it -> log.info("Saving task with title \"{}\" - ({})", it.getTitle(), LocalDateTime.now()))
                .flatMap(taskRepository::save);
    }
}
