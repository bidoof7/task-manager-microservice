package com.project.auth_service.service;

import com.project.auth_service.dto.*;
import com.project.auth_service.entity.Account;
import com.project.auth_service.entity.Role;
import com.project.auth_service.exception.UsernameNotFound;
import com.project.auth_service.jwt.JwtService;
import com.project.auth_service.repository.AccountRepository;
import com.project.auth_service.security.CustomUserDetails;
import com.project.auth_service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RoleService roleService;

    public RegisterResponse register(RegisterRequest request) {
        if (accountRepository.existsByUsername(request.username())) {
            throw new RuntimeException("USERNAME ALREADY EXISTS");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new RuntimeException("PASSWORD NOT EQUALS");
        }

        Role role = roleService.findByName("USER");

        Account account = new Account();
        account.setUsername(request.username());
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setRole(role);
        account.setStatus("ACTIVE");

        Account savedAccount = accountRepository.save(account);

        return new RegisterResponse(savedAccount.getUsername());
    }

    public LoginResult login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        assert userDetails != null;
        String accessToken = jwtService.generateToken(userDetails);

        Account account = accountRepository.findByUsername(request.username()).orElseThrow(() -> new UsernameNotFound("USERNAME NOT FOUND"));

        String refreshToken = refreshTokenService.create(account.getId());

        return new LoginResult(accessToken, refreshToken);
    }

    public LoginResult refresh(String oldToken) {
        Long oldAccountId = refreshTokenService.getAccountId(oldToken);

        refreshTokenService.revoke(oldToken);

        Account newAccount = accountRepository.findById(oldAccountId).orElseThrow(() -> new RuntimeException("ACCOUNT ID NOT FOUND"));

        UserDetails userDetails = new CustomUserDetails(newAccount);

        String accessToken = jwtService.generateToken(userDetails);

        String refreshToken = refreshTokenService.create(newAccount.getId());

        return new LoginResult(accessToken, refreshToken);
    }

    public void logout(String token) {
        refreshTokenService.revoke(token);
    }
}
