package com.projects.shift_scheduler_api.models;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class Manager extends Employee {
    private BigDecimal salary;

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "salary=" + salary +
                '}';
    }
}
