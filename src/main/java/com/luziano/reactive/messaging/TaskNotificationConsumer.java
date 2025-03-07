package com.luziano.reactive.messaging;

import com.luziano.reactive.model.Task;
import com.luziano.reactive.service.TaskService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TaskNotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(TaskNotificationConsumer.class);

    private final TaskService taskService;

    @KafkaListener(topics = "${kafka.task.notification.output}", groupId = "${kafka.task.notification-group.id}")
    public void receiveAndFinishTask(Task task) {
        Mono.just(task)
                .doOnNext(it -> log.info("Receiving task to finish. | Id: {}", task.getId()))
                .flatMap(taskService::done)
                .subscribe();
    }
}
