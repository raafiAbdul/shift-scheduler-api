package com.projects.shift_scheduler_api.models;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "employee_shift")
public class UserShift {
    @EmbeddedId
    private UserShiftKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_fk"))
    private User user;

    @ManyToOne
    @MapsId("shiftId")
    @JoinColumn(name = "shift_id", foreignKey = @ForeignKey(name = "shift_fk"))
    private Shift shift;

    @Column(name = "clocked_in")
    private OffsetDateTime clockedIn;

    @Column(name = "clocked_out")
    private OffsetDateTime clockedOut;

    @Column(name = "hours_worked")
    private double hoursWorked;

    public UserShiftKey getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
