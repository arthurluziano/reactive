package com.luziano.reactive.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse() {}

    public ErrorResponse(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
    }

    public static ErrorResponse internalError(RuntimeException ex) {
        return ErrorResponse.builder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withMessage(ex.getMessage())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builderFrom(ErrorResponse errorResponse) {
        return new Builder(errorResponse);
    }

    public static class Builder {
        private int status;
        private String message;

        public Builder(ErrorResponse errorResponse) {
            this.status = errorResponse.status;
            this.message = errorResponse.message;
        }

        public Builder() {}

        public Builder withStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
