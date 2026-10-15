package com.project.auth_service.service;

import com.project.auth_service.entity.Account;
import com.project.auth_service.entity.Permission;
import com.project.auth_service.exception.AccountNotFound;
import com.project.auth_service.repository.AccountRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AccountRepository accountRepository;

    public void issueTokens(
            Account account,
            HttpServletResponse response
    ) {
        String role = account.getRole().getRoleName();

        List<String> permissions = account.getRole()
                .getPermission()
                .stream()
                .map(Permission::getPermissionName)
                .toList();

        String accessToken =
                jwtService.generateAccessToken(account.getEmail(), role, permissions);

        String refreshToken =
                refreshTokenService.create(account.getId());

        setAccessTokenCookie(
                response,
                accessToken
        );

        setRefreshTokenCookie(
                response,
                refreshToken
        );
    }

    public void revokeRefreshToken(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }

    public void refresh(
            String oldRefreshToken,
            HttpServletResponse response
    ) {
        Long accountId =
                refreshTokenService.getAccountId(
                        oldRefreshToken
                );

        refreshTokenService.revoke(
                oldRefreshToken
        );

        Account account =
                accountRepository.findById(accountId)
                        .orElseThrow(() ->
                                new AccountNotFound(
                                        accountId
                                )
                        );

        issueTokens(
                account,
                response
        );
    }

    private void setAccessTokenCookie(
            HttpServletResponse response,
            String accessToken
    ) {
        ResponseCookie cookie = ResponseCookie
                .from("access_token", accessToken)
                .httpOnly(true)
                .secure(false) // true khi production HTTPS
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

    private void setRefreshTokenCookie(
            HttpServletResponse response,
            String refreshToken
    ) {
        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // true khi production HTTPS
                .sameSite("Lax")
                .path("/api/auth")
                .maxAge(Duration.ofDays(30))
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

    public void clearCookie(
            HttpServletResponse response,
            String name,
            String path
    ) {
        ResponseCookie cookie = ResponseCookie
                .from(name, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(path)
                .maxAge(0)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }
}