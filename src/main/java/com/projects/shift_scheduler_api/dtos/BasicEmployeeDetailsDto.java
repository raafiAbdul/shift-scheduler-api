package com.projects.shift_scheduler_api.dtos;

import com.projects.shift_scheduler_api.models.Employee;
import com.projects.shift_scheduler_api.models.Manager;
import com.projects.shift_scheduler_api.models.Role;
import com.projects.shift_scheduler_api.models.Worker;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BasicEmployeeDetailsDto {
    private String username;
    private String position;
    private Role role;
    private LocalDate dateHired;
    private BigDecimal payRate;
    private BigDecimal salary;

    public BasicEmployeeDetailsDto(Employee e) {
        this.username = e.getUsername();
        this.position = e.getPosition();
        this.role = e.getRole();
        this.dateHired = e.getDateHired();

        if(e instanceof Worker w)
            this.payRate = w.getPayRate();

        if(e instanceof Manager m)
            this.salary = m.getSalary();

    }

    public BigDecimal getPayRate() {
        return payRate;
    }

    public void setPayRate(BigDecimal payRate) {
        this.payRate = payRate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getDateHired() {
        return dateHired;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }
}
