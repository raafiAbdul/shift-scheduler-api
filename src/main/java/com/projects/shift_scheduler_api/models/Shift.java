package com.projects.shift_scheduler_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @NotNull
    @Size(min = 10, max = 1000)
    private String description;

    @Column(name = "required_employees_count")
    @Min(value = 1)
    private int requiredEmployeesCount;

    @Column(name = "start_time")
    @NotNull
    private OffsetDateTime startTime;

    @Column(name = "end_time")
    @NotNull
    private OffsetDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ShiftState state;

    @PositiveOrZero
    private int currentEmployeeCount = 0;

    @NotNull
    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeShift> employeeShifts = new ArrayList<>();

    public int getCurrentEmployeeCount() {
        return currentEmployeeCount;
    }

    public void setCurrentEmployeeCount(int currentEmployeeCount) {
        this.currentEmployeeCount = currentEmployeeCount;
    }

    public List<EmployeeShift> getEmployeeShifts() {
        return employeeShifts;
    }

    public void setEmployeeShifts(List<EmployeeShift> employeeShifts) {
        this.employeeShifts = employeeShifts;
    }

    public ShiftState getState() {
        return state;
    }

    public void setState(ShiftState state) {
        this.state = state;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredEmployeesCount() {
        return requiredEmployeesCount;
    }

    public void setRequiredEmployeesCount(int requiredEmployeesCount) {
        this.requiredEmployeesCount = requiredEmployeesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return id == shift.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Shift{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", description='" + description + '\'' +
                ", requiredEmployeesCount=" + requiredEmployeesCount +
                '}';
    }

    public void addEmployee(Employee employee) {
        if(employee == null) {
            throw new IllegalArgumentException("Employee doesn't exist");
        }
        if(this.getState() == ShiftState.FULL ||
                this.getState() == ShiftState.IN_PROGRESS ||
                this.getState() == ShiftState.CLOSED) {
            throw new IllegalStateException("Shift is in-progress, full or closed");
        }
        if(!Collections.disjoint(this.employeeShifts, employee.getEmployeeShifts())) {
            throw new IllegalStateException("Already taking shift");
        }
        EmployeeShift employeeShift = new EmployeeShift(new EmployeeShiftKey(this.id, employee.getId()));
        employeeShift.setEmployee(employee);
        employeeShift.setShift(this);
        this.getEmployeeShifts().add(employeeShift);
        employee.getEmployeeShifts().add(employeeShift);
        this.setCurrentEmployeeCount(this.getCurrentEmployeeCount() + 1);
    }

    public void removeEmployee(Employee employee) {
        if(employee == null) {
            throw new IllegalArgumentException("Employee doesn't exist");
        }
        if(this.getState() == ShiftState.IN_PROGRESS || this.getState() == ShiftState.CLOSED) {
            throw new IllegalStateException("Shift is in-progress or closed");
        }
        if(Collections.disjoint(this.employeeShifts, employee.getEmployeeShifts())) {
            throw new IllegalStateException("Has not taken shift");
        }
        if(this.employeeShifts.isEmpty()) {
            throw new IllegalStateException("No employees taking the shift");
        }
        if(employee.getEmployeeShifts().isEmpty()) {
            throw new IllegalStateException("Employee has not taken any shifts");
        }
        EmployeeShift employeeShift = new EmployeeShift(new EmployeeShiftKey(this.id, employee.getId()));
        employee.getEmployeeShifts().remove(employeeShift);
        this.getEmployeeShifts().remove(employeeShift);
        this.setCurrentEmployeeCount(this.getCurrentEmployeeCount() - 1);
    }
}


