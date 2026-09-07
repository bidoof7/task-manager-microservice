package com.project.auth_service.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty
        String username,

        @NotEmpty
        String password
) {
}
