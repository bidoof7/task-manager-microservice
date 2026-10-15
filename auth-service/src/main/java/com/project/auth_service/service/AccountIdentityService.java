package com.project.auth_service.service;

import com.project.auth_service.entity.Account;
import com.project.auth_service.entity.AccountIdentity;
import com.project.auth_service.entity.Role;
import com.project.auth_service.enums.AuthProvider;
import com.project.auth_service.repository.AccountIdentityRepository;
import com.project.auth_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountIdentityService {
    private final AccountIdentityRepository accountIdentityRepository;
    private final AccountRepository accountRepository;
    private final RoleService roleService;

    public Account identityAccount(OidcUser googleUser) {
        String subject = googleUser.getSubject();

        return accountIdentityRepository.findByProviderAndProviderSubject(AuthProvider.GOOGLE, subject)
                .map(AccountIdentity::getAccount)
                .orElseGet(() -> createGoogleAccount(
                        googleUser
                ));
    }

    private Account createGoogleAccount(
            OidcUser googleUser
    ) {
        String email = googleUser.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalStateException(
                    "Google account does not provide an email"
            );
        }

        if (!Boolean.TRUE.equals(
                googleUser.getEmailVerified()
        )) {
            throw new IllegalStateException(
                    "Google email has not been verified"
            );
        }

        if (accountRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException(
                    "An account with this email already exists. " +
                            "Please login with your existing account and link Google."
            );
        }

        Role role = roleService.findByName("USER");

        Account account = new Account();
        account.setEmail(email);
        account.setPasswordHash(null);
        account.setEmailVerified(true);
        account.setRole(role);
        account.setStatus("ACTIVE");

        account = accountRepository.save(account);

        AccountIdentity identity = new AccountIdentity();
        identity.setAccount(account);
        identity.setProvider(AuthProvider.GOOGLE);
        identity.setProviderSubject(
                googleUser.getSubject()
        );

        accountIdentityRepository.save(identity);

        return account;
    }
}
