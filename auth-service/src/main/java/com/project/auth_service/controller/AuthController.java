package com.project.auth_service.controller;

import com.project.auth_service.dto.Response;
import com.project.auth_service.dto.*;
import com.project.auth_service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    private final AccountService _accountService;

    @PostMapping("/register")
    public ResponseEntity<Response<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(new Response<>(true, "ACCOUNT REGISTER SUCCESSFULLY", _accountService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = _accountService.login(request);

        ResponseCookie cookie = createRefreshCookie(result.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new Response<>(true, "LOGIN ACCOUNT SUCCESSFULLY", new LoginResponse(result.accessToken())));
    }

    @Operation(
            summary = "Refresh access token",
            description = """
                Generate a new access token using the
                refresh token stored in an HttpOnly cookie.
                """
    )
    @PostMapping("/refresh")
    public ResponseEntity<Response<LoginResponse>> refresh(@Parameter(hidden = true) @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new RuntimeException("REFRESH TOKEN IS NULL");
        }

        LoginResult result = _accountService.refresh(refreshToken);

        ResponseCookie cookie = createRefreshCookie(result.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new Response<>(true, "REFRESH TOKEN SUCCESSFULLY", new LoginResponse(result.accessToken())));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(@Parameter(hidden = true) @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken != null) {
            _accountService.logout(refreshToken);
        }

        ResponseCookie cookie = clearRefreshToken();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new Response<>(true, "LOGOUT SUCCESSFULLY", null));
    }

    private ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie
                .from("refresh_token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
    }

    private ResponseCookie clearRefreshToken() {
        return ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
