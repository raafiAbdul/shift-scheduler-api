package com.projects.shift_scheduler_api.dtos;

import java.time.OffsetDateTime;

public class ErrorDetailsDto<D> {
    private String error;
    private int status;
    private D message;
    private OffsetDateTime timestamp;

    public ErrorDetailsDto(String error, int status, D message) {
        this.error = error;
        this.status = status;
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public D getMessage() {
        return message;
    }

    public void setMessage(D message) {
        this.message = message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
