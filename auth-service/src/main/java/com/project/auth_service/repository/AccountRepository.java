package com.project.auth_service.repository;

import com.project.auth_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
        SELECT DISTINCT a
        FROM Account a
        JOIN FETCH a.role r
        LEFT JOIN FETCH r.permission
        WHERE a.email = :email
    """)
    Optional<Account> findByEmailWithPermissions(
            @Param("email") String email
    );
}
