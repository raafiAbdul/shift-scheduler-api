package com.projects.shift_scheduler_api.dtos;

import com.projects.shift_scheduler_api.models.EmployeeShift;
import com.projects.shift_scheduler_api.models.ShiftState;

import java.time.OffsetDateTime;
import java.util.List;

public class ShiftDto {
    private long id;
    private String description;
    private int requiredEmployeesCount;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private ShiftState state;
    private int currentEmployeeCount;
    private List<EmployeeShift> employeeShifts;

    public ShiftDto() {
    }

    public ShiftDto(long id, String description,
                    int requiredEmployeesCount,
                    OffsetDateTime startTime,
                    OffsetDateTime endTime,
                    ShiftState state,
                    int currentEmployeeCount,
                    List<EmployeeShift> employeeShifts) {
        this.id = id;
        this.description = description;
        this.requiredEmployeesCount = requiredEmployeesCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.currentEmployeeCount = currentEmployeeCount;
        this.employeeShifts = employeeShifts;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredEmployeesCount() {
        return requiredEmployeesCount;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public ShiftState getState() {
        return state;
    }

    public int getCurrentEmployeeCount() {
        return currentEmployeeCount;
    }

    public List<EmployeeShift> getEmployeeShifts() {
        return employeeShifts;
    }
}
