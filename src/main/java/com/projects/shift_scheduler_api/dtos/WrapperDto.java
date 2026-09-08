package com.projects.shift_scheduler_api.dtos;

public class WrapperDto<T> {
    private T type;
    private int status;
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
