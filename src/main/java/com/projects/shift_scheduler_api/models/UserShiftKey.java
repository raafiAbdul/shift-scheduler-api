package com.projects.shift_scheduler_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserShiftKey implements Serializable {
    @Column(name = "shift_id")
    private long shiftId;

    @Column(name = "user_id")
    private long userId;

    public long getShiftId() {
        return shiftId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserShiftKey that = (UserShiftKey) o;
        return shiftId == that.shiftId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shiftId, userId);
    }
}
