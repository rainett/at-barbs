package com.rainett.atbarbsbackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    @Getter
    private final HttpStatus httpStatus;

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
