package com.luziano.reactive.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Document
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;
    private Address address;
    private LocalDateTime createdAt;

    public Task(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.state = builder.state;
        this.address = builder.address;
        this.createdAt = builder.createdAt;
    }

    public Task() {}

    public Task insert() {
        return builderFrom(this)
                .withState(TaskState.INSERT)
                .withCreatedAt(LocalDateTime.now())
                .build();
    }

    public Task update(Task oldTask) {
        return builderFrom(this)
                .withId(oldTask.getId())
                .withState(oldTask.getState())
                .build();
    }

    public Task updateAddress(Address address) {
        return builderFrom(this)
                .withAddress(address)
                .build();
    }

    public Task start() {
        return builderFrom(this)
                .withState(TaskState.DOING)
                .build();
    }

    public Task done() {
        return builderFrom(this)
                .withState(TaskState.DONE)
                .build();
    }

    public Task createdNow() {
        return builderFrom(this)
                .withCreatedAt(LocalDateTime.now())
                .build();
    }

    public Boolean createdAtIsEmpty() {
        return this.createdAt == null;
    }

    public Boolean isValidState() {
        return this.state != null;
    }

    public Boolean isValidPriority() {
        return this.priority > 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(Task task) {
        return new Builder(task);
    }

    public static class Builder {
        private String id;
        private String title;
        private String description;
        private int priority;
        private TaskState state;
        private Address address;
        private LocalDateTime createdAt;

        public Builder(Task task) {
            this.id = task.id;
            this.title = task.title;
            this.description = task.description;
            this.priority = task.priority;
            this.state = task.state;
            this.address = task.address;
            this.createdAt = task.createdAt;
        }

        public Builder() {}

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder withState(TaskState state) {
            this.state = state;
            return this;
        }

        public Builder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
