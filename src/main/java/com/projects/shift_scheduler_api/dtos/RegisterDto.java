package com.projects.shift_scheduler_api.dtos;

import com.projects.shift_scheduler_api.models.Role;
import jakarta.validation.constraints.*;

public class RegisterDto {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    private String username;

    @Email(
            regexp = "[\\w\\.-]{2,30}@[\\w\\.-]{2,20}\\.[a-z[A-Z]]{2,10}",
            message = "Please enter a valid email such as user@domain.com"
    )
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 100)
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\p{P})[\\w[\\p{P}]]{8,}",
            message = "Should contain punctuation, a number, lower and upper case letters.")
    private String password;

    @NotNull
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
