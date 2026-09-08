package com.projects.shift_scheduler_api.models;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "employee_shift")
public class EmployeeShift {
    @EmbeddedId
    private EmployeeShiftKey id;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id", foreignKey = @ForeignKey(name = "employee_fk"))
    private Employee employee;

    @ManyToOne
    @MapsId("shiftId")
    @JoinColumn(name = "shift_id", foreignKey = @ForeignKey(
            name = "shift_fk",
            foreignKeyDefinition = "foreign key (employee_id) references employees(id) on delete cascade"
    ))
    private Shift shift;

    @Column(name = "clocked_in")
    private OffsetDateTime clockedIn;

    @Column(name = "clocked_out")
    private OffsetDateTime clockedOut;

    @Column(name = "hours_worked")
    private double hoursWorked = 0;

    public EmployeeShift() {
    }

    public EmployeeShift(EmployeeShiftKey id) {
        this.id = id;
    }

    public EmployeeShiftKey getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public OffsetDateTime getClockedIn() {
        return clockedIn;
    }

    public void setClockedIn(OffsetDateTime clockedIn) {
        this.clockedIn = clockedIn;
    }

    public OffsetDateTime getClockedOut() {
        return clockedOut;
    }

    public void setClockedOut(OffsetDateTime clockedOut) {
        this.clockedOut = clockedOut;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setId(EmployeeShiftKey id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeShift that = (EmployeeShift) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
