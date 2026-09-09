package com.projects.shift_scheduler_api.dtos;

import jakarta.validation.constraints.NotNull;

public class StartAndEndTimeDto {
    @NotNull
    private ShiftDateDto startTime;
    @NotNull
    private ShiftDateDto endTime;

    public StartAndEndTimeDto() {
    }

    public ShiftDateDto getEndTime() {
        return endTime;
    }

    public void setStartTime(ShiftDateDto startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(ShiftDateDto endTime) {
        this.endTime = endTime;
    }

    public ShiftDateDto getStartTime() {
        return startTime;


    }
}
