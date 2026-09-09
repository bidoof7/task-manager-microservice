package com.project.task_service.utils;

import com.project.task_service.entity.Task;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SecurityUtils {
    public String getCurrentUsername() {
        Authentication authentication = getAuthenticationUser();
        return authentication.getName();
    }

    private Authentication getAuthenticationUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("USER IS NOT AUTHENTICATED");
        }

        return authentication;
    }


    public boolean isAdmin() {
        Authentication authentication = getAuthenticationUser();

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(
                        authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN")
                );
    }

}
