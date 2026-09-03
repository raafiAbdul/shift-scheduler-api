package com.projects.shift_scheduler_api.models;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "shifts")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;

    @Column(name = "required_employees_count")
    private int requiredEmployeesCount;

    @Column(name = "start_time")
    private OffsetDateTime startTime;

    @Column(name = "end_time")
    private OffsetDateTime endTime;
    private ShiftState state;

    @OneToMany(mappedBy = "shift")
    private Set<UserShift> userShifts;

    public Set<UserShift> getUserShifts() {
        return userShifts;
    }

    public void setUserShifts(Set<UserShift> userShifts) {
        this.userShifts = userShifts;
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
}


