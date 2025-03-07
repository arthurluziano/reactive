package com.luziano.reactive.controller;

import com.luziano.reactive.model.TaskState;
import com.luziano.reactive.model.converter.TaskDTOConverter;
import com.luziano.reactive.model.converter.TaskInsertDTOConverter;
import com.luziano.reactive.model.converter.TaskUpdateDTOConverter;
import com.luziano.reactive.model.dto.TaskDTO;
import com.luziano.reactive.model.dto.TaskInsertDTO;
import com.luziano.reactive.model.dto.TaskUpdateDTO;
import com.luziano.reactive.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {

    private final static Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;
    private final TaskDTOConverter converter;
    private final TaskInsertDTOConverter insertDTOConverter;
    private final TaskUpdateDTOConverter updateDTOConverter;

    @GetMapping
    public Mono<Page<TaskDTO>> getTasks(@RequestParam(required = false) String id,
                                        @RequestParam(required = false) String title,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(required = false, defaultValue = "0") int priority,
                                        @RequestParam(required = false) TaskState state,
                                        @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        return taskService.findPaginated(converter.convert(id, title, description, priority, state), pageNumber, pageSize)
                .map(it -> it.map(converter::convert));
    }

    @PostMapping
    public Mono<TaskDTO> createTask(@RequestBody @Valid TaskInsertDTO taskInsertDTO) {
        return taskService.insert(insertDTOConverter.convert(taskInsertDTO))
                .map(converter::convert);
    }

    @PutMapping("/{id}")
    public Mono<TaskDTO> updateTask(@RequestParam String id, @RequestBody @Valid TaskUpdateDTO taskUpdateDTO) {
        return taskService.update(id, updateDTOConverter.convert(taskUpdateDTO))
                .doOnNext(it -> log.info("Update task with id \"{}\" - ({})", it.getId(), LocalDateTime.now()))
                .map(converter::convert);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTask(@PathVariable String id) {
        return Mono.just(id)
                .doOnNext(it -> log.info("Deleted task with id \"{}\" - ({})", it, LocalDateTime.now()))
                .flatMap(taskService::deleteById);
    }

    @PostMapping("/start")
    public Mono<TaskDTO> start(@RequestParam String id, @RequestParam String zipCode) {
        return taskService.start(id, zipCode)
                .map(converter::convert);
    }

    @PostMapping("/refresh/createdAt")
    public Flux<TaskDTO> refreshCreatedAt() {
        return taskService.refreshCreatedAt()
                .map(converter::convert);
    }

    @PostMapping("/done")
    public Mono<List<TaskDTO>> done(@RequestBody List<String> ids) {
        return taskService.doneMany(ids)
                .map(converter::convertList);
    }
}
