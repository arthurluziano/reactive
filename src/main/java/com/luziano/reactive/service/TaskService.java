package com.luziano.reactive.service;

import com.luziano.reactive.exception.CepNotFoundException;
import com.luziano.reactive.exception.TaskNotFoundException;
import com.luziano.reactive.messaging.TaskNotificationProducer;
import com.luziano.reactive.model.Address;
import com.luziano.reactive.model.Task;
import com.luziano.reactive.repository.TaskCustomRepository;
import com.luziano.reactive.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final static Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskCustomRepository taskCustomRepository;
    private final AddressService addressService;
    private final TaskNotificationProducer producer;

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

    public Mono<Task> start(String id, String zipCode) {
        return taskRepository.findById(id)
                .zipWhen(it -> addressService.getAddress(zipCode))
                .flatMap(it -> updateAddress(it.getT1(), it.getT2()))
                .map(Task::start)
                .flatMap(taskRepository::save)
                .flatMap(producer::sendNotification)
                .switchIfEmpty(Mono.error(TaskNotFoundException::new))
                .doOnError(error -> log.error("Error on start task. ID: {} - ({})", id, LocalDateTime.now(), error));
    }

    public Mono<Task> done(Task task) {
        return Mono.just(task)
                .doOnNext(it -> log.info("Done. | Id: {}", task.getId()))
                .map(Task::done)
                .flatMap(taskRepository::save);
    }

    public Mono<List<Task>> doneMany(List<String> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> taskRepository.findById(id)
                        .map(Task::done)
                        .flatMap(taskRepository::save)
                        .doOnNext(it -> log.info("Finishing task. | Id: {}", it.getId()))
                ).collectList();
    }

    public Flux<Task> refreshCreatedAt() {
        return taskRepository.findAll()
                .filter(Task::createdAtIsEmpty)
                .map(Task::createdNow)
                .flatMap(taskRepository::save);
    }

    private Mono<Task> updateAddress(Task task, Address address) {
        return Mono.just(task)
                .flatMap(it -> hasNullFields(address) ? Mono.error(CepNotFoundException::new) : Mono.just(it))
                .map(it -> task.updateAddress(address));
    }

    private Mono<Task> save(Task task) {
        return Mono.just(task)
                .doOnNext(it -> log.info("Saving task with title \"{}\" - ({})", it.getTitle(), LocalDateTime.now()))
                .flatMap(taskRepository::save);
    }

    private boolean hasNullFields(Address address) {
        return address == null ||
                address.getStreet() == null ||
                address.getCity() == null ||
                address.getState() == null ||
                address.getZipCode() == null ||
                address.getComplement() == null;
    }
}
