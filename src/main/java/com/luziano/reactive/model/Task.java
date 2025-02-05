package com.luziano.reactive.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private int priority;
    private TaskState state;

    public Task(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.state = builder.state;
    }

    public Task() {}

    public Task insert() {
        return builderFrom(this)
                .withState(TaskState.INSERT)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(Task task) {
        return new Builder(task);
    }

    public static class Builder {
        private String title;
        private String description;
        private int priority;
        private TaskState state;

        public Builder(Task task) {
            this.title = task.title;
            this.description = task.description;
            this.priority = task.priority;
            this.state = task.state;
        }

        public Builder() {}

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

        public Task build() {
            return new Task(this);
        }
    }
}
