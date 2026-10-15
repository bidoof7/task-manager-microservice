package com.project.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Must be email (abc@gmail.com)")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password should be longer than 8 characters.")
        String password
) {
}
