package com.project.auth_service.dto;

public record LoginResult(
        String accessToken,
        String refreshToken
) {
}
