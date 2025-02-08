package com.luziano.reactive.repository;

import com.luziano.reactive.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class TaskCustomRepository {

    private final ReactiveMongoOperations mongoOperations;

    public Mono<Page<Task>> findPaginated(Task task, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("priority", "state");

        Example<Task> example = Example.of(task, matcher);

        Query query = Query.query(Criteria.byExample(example)).with(pageable);

        if(task.getPriority() > 0) {
            query.addCriteria(Criteria.where("priority").is(task.getPriority()));
        }

        if(task.getState() != null) {
            query.addCriteria(Criteria.where("state").is(task.getState()));
        }

        return mongoOperations.find(query, Task.class)
                .collectList()
                .zipWith(mongoOperations.count(Query.query(Criteria.byExample(example)), Task.class))
                .map(tuple -> PageableExecutionUtils.getPage(tuple.getT1(), pageable, tuple::getT2));
    }
}
