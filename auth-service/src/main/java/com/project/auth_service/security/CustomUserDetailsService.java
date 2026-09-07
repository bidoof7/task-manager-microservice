package com.project.auth_service.security;

import com.project.auth_service.entity.Account;
import com.project.auth_service.exception.UsernameNotFound;
import com.project.auth_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository _accountRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Account account = _accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFound("USER NOT FOUND"));
        return new CustomUserDetails(account);
    }
}
