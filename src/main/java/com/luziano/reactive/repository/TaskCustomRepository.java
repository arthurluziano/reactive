package com.luziano.reactive.repository;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.model.TaskState;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Repository
@AllArgsConstructor
public class TaskCustomRepository {

    private final ReactiveMongoOperations mongoOperations;
    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        return queryExample(task)
                .zipWith(pageable(pageNumber, pageSize))
                .flatMap(it -> execute(task, it.getT1(), it.getT2()));
    }

    public Mono<Long> updateStateToDoneForOlderTasks(LocalDate date) {
        return mongoTemplate.updateMulti(
                Query.query(Criteria
                        .where("createdAt").lte(date) // lte = Less Then or Equal.
                        .and("state").is(TaskState.DOING)),
                Update.update("state", TaskState.DONE),
                Task.class
        ).map(UpdateResult::getModifiedCount);
    }

    private Mono<Page<Task>> execute(Task task, Example<Task> example, Pageable pageable) {
        return query(example, pageable, task)
                .flatMap(query -> mongoOperations.find(query, Task.class).collectList())
                .flatMap(tasks -> paginate(tasks, pageable, example));
    }

    private Mono<Page<Task>> paginate(List<Task> tasks, Pageable pageable, Example<Task> example) {
        return Mono.just(tasks)
                .flatMap(it -> mongoOperations.count(Query.query(Criteria.byExample(example)), Task.class))
                .map(counter -> PageableExecutionUtils.getPage(tasks, pageable, () -> counter));
    }

    private Mono<Pageable> pageable(Integer pageNumber, Integer pageSize) {
        return Mono.just(PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending()));
    }

    private Mono<Example<Task>> queryExample(Task task) {
        return Mono.just(task)
                .map(it -> ExampleMatcher.matching().withIgnorePaths("priority", "state"))
                .map(matcher -> Example.of(task, matcher));
    }

    private Mono<Query> query(Example<Task> example, Pageable pageable, Task task) {
        return Mono.just(example)
                .map(ex -> Query.query(Criteria.byExample(example)).with(pageable))
                .flatMap(query -> validatePriority(query, task))
                .flatMap(query -> validateState(query, task));
    }

    private Mono<Query> validatePriority(Query query, Task task) {
        return Mono.just(task)
                .filter(Task::isValidPriority)
                .map(it -> query.addCriteria(Criteria.where("priority").is(it.getPriority())))
                .defaultIfEmpty(query);
    }

    private Mono<Query> validateState(Query query, Task task) {
        return Mono.just(task)
                .filter(Task::isValidState)
                .map(it -> query.addCriteria(Criteria.where("state").is(it.getState())))
                .defaultIfEmpty(query);
    }
}
