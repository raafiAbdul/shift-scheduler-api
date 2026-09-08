package com.projects.shift_scheduler_api.dtos;

public class ErrorDetailsDto<T> {
    private T type;
    private int status;
    private String[] details;

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getDetails() {
        return details;
    }

    public void setDetails(String[] details) {
        this.details = details;
    }
}
