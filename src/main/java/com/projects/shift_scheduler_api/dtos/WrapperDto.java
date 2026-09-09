package com.projects.shift_scheduler_api.dtos;

public final class WrapperDto<T> {
    private final T data;
    private final int status;
    private final String message;

    public WrapperDto(T data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
