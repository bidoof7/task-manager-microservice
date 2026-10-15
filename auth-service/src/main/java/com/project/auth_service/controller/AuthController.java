package com.project.auth_service.controller;

import com.project.auth_service.dto.Response;
import com.project.auth_service.dto.*;
import com.project.auth_service.exception.InvalidRefreshTokenException;
import com.project.auth_service.service.AccountService;
import com.project.auth_service.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Tag(
        name = "Authentication",
        description = "Authentication and Authorization APIs"
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountService accountService;
    private final TokenService tokenService;

    @GetMapping("/me")
    public ResponseEntity<AccountMeResponse> me(Authentication authentication) {
        return ResponseEntity.ok(accountService.getCurrentUser(authentication));
    }

    @PostMapping("/register")
    public ResponseEntity<Response<Void>> register(@Valid @RequestBody RegisterRequest request) {
        accountService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Response<Void>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        accountService.login(request, response);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Refresh access token",
            description = """
                    Generate a new access token using the
                    refresh token stored in an HttpOnly cookie.
                    """
    )
    @PostMapping("/refresh")
    public ResponseEntity<Response<Void>> refresh(@Parameter(hidden = true) @CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException();
        }
        tokenService.refresh(refreshToken, response);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(@Parameter(hidden = true) @CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            accountService.logout(refreshToken, response);
        }

        return ResponseEntity.noContent().build();
    }
}
