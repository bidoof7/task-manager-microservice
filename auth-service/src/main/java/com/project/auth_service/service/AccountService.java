package com.project.auth_service.service;

import com.project.auth_service.dto.*;
import com.project.auth_service.entity.Account;
import com.project.auth_service.entity.Role;
import com.project.auth_service.exception.EmailNotFound;
import com.project.auth_service.repository.AccountRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final AccountIdentityService accountIdentityService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final OutboxEventService outboxEventService;

    public AccountMeResponse getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        Account account = accountRepository.findByEmailWithPermissions(email).orElseThrow(() -> new EmailNotFound(email));

        return new AccountMeResponse(
                account.getId(),
                account.getEmail(),
                account.getRole().getRoleName()
        );
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new RuntimeException("USERNAME ALREADY EXISTS");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new RuntimeException("PASSWORD NOT EQUALS");
        }

        Role role = roleService.findByName("USER");

        Account account = new Account();
        account.setEmail(request.email());
        account.setEmailVerified(false);
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setRole(role);
        account.setStatus("ACTIVE");

        Account savedAccount = accountRepository.save(account);

        outboxEventService.saveAccountCreatedEvent(savedAccount);
    }

    public void login(LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        Account account = accountRepository
                .findByEmailWithPermissions(request.email())
                .orElseThrow(() -> new EmailNotFound(request.email()));

        tokenService.issueTokens(account, response);
    }

    @Transactional
    public void loginWithGoogle(
            OidcUser googleUser,
            HttpServletResponse response
    ) {
        Account account = accountIdentityService.identityAccount(googleUser);

        tokenService.issueTokens(account, response);
    }

    public void logout(String refreshToken, HttpServletResponse response) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            tokenService.revokeRefreshToken(refreshToken);
        }

        tokenService.clearCookie(response, "access_token", "/");
        tokenService.clearCookie(response, "refresh_token", "/api/v1/auth/refresh");
    }
}
