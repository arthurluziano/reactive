package com.luziano.reactive.service;

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
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final static Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskCustomRepository taskCustomRepository;

    public Mono<Task> insert(Task task) {
        return Mono.just(task)
                .map(Task::insert)
                .flatMap(this::save);
    }

    public Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        return taskCustomRepository.findPaginated(task, pageNumber, pageSize)
                .doOnNext(it -> log.info("Listing tasks... - ({})", LocalDateTime.now()));
    }

    private Mono<Task> save(Task task) {
        return Mono.just(task)
                .doOnNext(it -> log.info("Saving task with title \"{}\" - ({})", it.getTitle(), LocalDateTime.now()))
                .flatMap(taskRepository::save);
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }
}
