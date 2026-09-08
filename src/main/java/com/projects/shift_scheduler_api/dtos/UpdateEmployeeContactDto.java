package com.projects.shift_scheduler_api.dtos;

import jakarta.validation.constraints.*;

public class UpdateEmployeeContactDto {

    @PositiveOrZero
    private long id;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @Email(
            regexp = "[\\w\\.-]{2,30}@[\\w\\.-]{2,20}\\.[a-z[A-Z]]{2,10}",
            message = "Please enter a valid email such as user@domain.com"
    )
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
