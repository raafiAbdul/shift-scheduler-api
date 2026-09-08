package com.projects.shift_scheduler_api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdminLoginDto {
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

    @NotNull
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;


}
