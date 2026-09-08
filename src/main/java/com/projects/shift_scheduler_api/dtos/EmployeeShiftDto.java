package com.projects.shift_scheduler_api.dtos;

import jakarta.validation.constraints.PositiveOrZero;

public class EmployeeShiftDto {
    @PositiveOrZero
    private long shiftId;
    @PositiveOrZero
    private long employeeId;

    public long getShiftId() {
        return shiftId;
    }

    public void setShiftId(long shiftId) {
        this.shiftId = shiftId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
}
