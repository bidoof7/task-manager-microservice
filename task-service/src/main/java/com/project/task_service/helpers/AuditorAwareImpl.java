package com.project.task_service.helpers;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authencation = SecurityContextHolder.getContext().getAuthentication();

        if (authencation == null || !authencation.isAuthenticated()) {
            return Optional.empty();
        }

        return Optional.of(authencation.getName());
    }
}
