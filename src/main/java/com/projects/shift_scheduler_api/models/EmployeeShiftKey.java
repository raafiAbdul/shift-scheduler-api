package com.projects.shift_scheduler_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeeShiftKey implements Serializable {
    @Column(name = "shift_id")
    @Size
    private long shiftId;

    @Column(name = "employee_id")
    private long employeeId;

    public EmployeeShiftKey() {
    }

    public EmployeeShiftKey(long shiftId, long employeeId) {
        this.shiftId = shiftId;
        this.employeeId = employeeId;
    }

    public long getShiftId() {
        return shiftId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeShiftKey that = (EmployeeShiftKey) o;
        return shiftId == that.shiftId && employeeId == that.employeeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shiftId, employeeId);
    }
}
