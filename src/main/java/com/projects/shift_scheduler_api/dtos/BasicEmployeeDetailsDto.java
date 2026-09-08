package com.projects.shift_scheduler_api.dtos;

import com.projects.shift_scheduler_api.models.Role;

import java.time.LocalDate;

public class BasicEmployeeDetailsDto {
    private String username;
    private String position;
    private Role role;
    private LocalDate dateHired;

    public BasicEmployeeDetailsDto(String username, String position, Role role, LocalDate dateHired) {
        this.username = username;
        this.position = position;
        this.role = role;
        this.dateHired = dateHired;
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
