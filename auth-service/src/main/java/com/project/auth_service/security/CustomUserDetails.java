package com.project.auth_service.security;

import com.project.auth_service.entity.Account;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));
        Set<GrantedAuthority> authorities = new HashSet<>();

        // ROLE
        authorities.add(new SimpleGrantedAuthority("ROLE_" + account.getRole().getRoleName()));

        // PERMISSION
        account
                .getRole()
                .getPermission()
                .forEach(
                        permission -> authorities.add(
                                new SimpleGrantedAuthority(
                                        permission.getPermissionName()
                                )
                        )
                );

        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(account.getStatus());
    }
}
