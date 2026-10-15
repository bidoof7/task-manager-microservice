package com.project.auth_service.exception;

public class AccountNotFound extends RuntimeException {
    public AccountNotFound(Long id) {
        super("ACCOUNT ID: " + id + " NOT FOUND");
    }
}
