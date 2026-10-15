package com.project.auth_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotEmpty
        String email,

        @NotEmpty
        @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 kí tự")
        String password,

        @NotEmpty
        @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 kí tự")
        String confirmPassword
) {
};
