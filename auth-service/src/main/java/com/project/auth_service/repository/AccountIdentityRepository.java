package com.project.auth_service.repository;

import com.project.auth_service.entity.AccountIdentity;
import com.project.auth_service.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountIdentityRepository extends JpaRepository<AccountIdentity, Long> {
    Optional<AccountIdentity> findByProviderAndProviderSubject(
            AuthProvider provider,
            String providerSubject
    );
}