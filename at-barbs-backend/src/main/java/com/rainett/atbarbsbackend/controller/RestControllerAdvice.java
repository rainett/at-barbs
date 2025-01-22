package com.rainett.atbarbsbackend.controller;

import com.rainett.atbarbsbackend.dto.ErrorDto;
import com.rainett.atbarbsbackend.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestControllerAdvice {
    @ResponseBody
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorDto> handleException(AppException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorDto(exception.getMessage()));
    }
}
