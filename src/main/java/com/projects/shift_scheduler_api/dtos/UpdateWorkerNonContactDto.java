package com.projects.shift_scheduler_api.dtos;

import com.projects.shift_scheduler_api.models.Role;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class UpdateWorkerNonContactDto {

    @NotNull
    private Role role;

    @NotNull
    @NotBlank
    private String position;

    @NotNull
    @PositiveOrZero
    private BigDecimal balance;

    @NotNull
    @Positive
    private BigDecimal payRate;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }
}
