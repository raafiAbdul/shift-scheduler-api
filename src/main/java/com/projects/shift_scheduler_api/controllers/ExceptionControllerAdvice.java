package com.projects.shift_scheduler_api.controllers;

import com.projects.shift_scheduler_api.dtos.ErrorDetailsDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleNullPointerException(NullPointerException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetailsDto<String> ed = new ErrorDetailsDto<>(
                "NullPointerException", status.value(), e.getMessage());
        return ResponseEntity.status(status).body(ed);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorDetailsDto<String> ed = new ErrorDetailsDto<>(
                "NoSuchElementException", status.value(), e.getMessage());
        return ResponseEntity.status(status).body(ed);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetailsDto<String> ed = new ErrorDetailsDto<>(
                "IllegalStateException", status.value(), e.getMessage());
        return ResponseEntity.status(status).body(ed);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetailsDto<String> ed = new ErrorDetailsDto<>(
                "IllegalArgumentException", status.value(), e.getMessage());
        return ResponseEntity.status(status).body(ed);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDetailsDto<Map<String, String>> ed = new ErrorDetailsDto<>(
                "ConstraintViolationException", status.value(), constraintViolationExceptionHelperMethod(e));
        return ResponseEntity.status(status).body(ed);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        ErrorDetailsDto<Map<String, String>> ed = new ErrorDetailsDto<>(
                "MethodArgumentNotValidException", status.value(), fieldErrors);
        return ResponseEntity.status(status).body(ed);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDetailsDto<String> ed = new ErrorDetailsDto<>(
                "Exception", status.value(), e.getMessage());
        return ResponseEntity.status(status).body(ed);
    }

    private Map<String, String> constraintViolationExceptionHelperMethod(
            ConstraintViolationException cve
    ) {
        Map<String, String> violations = new HashMap<>();
        cve.getConstraintViolations().forEach(violation ->
            violations.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return violations;
    }


}
