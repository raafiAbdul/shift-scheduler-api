package com.projects.shift_scheduler_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class Worker extends Employee {

    @Column(name = "pay_rate")
    private BigDecimal payRate;

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "payRate=" + payRate +
                '}';
    }
}
