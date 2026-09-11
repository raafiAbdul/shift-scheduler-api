package com.projects.shift_scheduler_api.dtos;

import com.projects.shift_scheduler_api.models.ShiftState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateOrUpdateShiftDto {
    @NotNull @NotBlank private String description;
    @Min(value = 1) private int requiredEmployeeCount;
    @NotNull private ShiftDateDto startTime;
    @NotNull private ShiftDateDto endTime;
    @NotNull private ShiftState state = ShiftState.UPCOMING;

    public CreateOrUpdateShiftDto() {
    }

    public CreateOrUpdateShiftDto(String description, int requiredEmployeeCount, ShiftDateDto startTime, ShiftDateDto endTime) {
        this.description = description;
        this.requiredEmployeeCount = requiredEmployeeCount;
        this.startTime = startTime;
        this.endTime = endTime;

        if(ShiftDateDto.differenceInHours(endTime, startTime) > 96.00) {
            throw new IllegalArgumentException("Cannot have shift longer than 96 hours");
        }

    }

    public ShiftState getState() {
        return state;
    }

    public void setState(ShiftState state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredEmployeeCount() {
        return requiredEmployeeCount;
    }

    public void setRequiredEmployeeCount(int requiredEmployeeCount) {
        this.requiredEmployeeCount = requiredEmployeeCount;
    }

    public ShiftDateDto getStartTime() {
        return startTime;
    }

    public void setStartTime(ShiftDateDto startTime) {
        this.startTime = startTime;
    }

    public ShiftDateDto getEndTime() {
        return endTime;
    }

    public void setEndTime(ShiftDateDto endTime) {
        this.endTime = endTime;
    }
}
