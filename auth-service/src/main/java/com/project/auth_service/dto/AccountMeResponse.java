package com.project.auth_service.dto;

public record AccountMeResponse(
        Long id,
        String email,
        String role
) {
}
