package com.project.auth_service.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String email) {
        super("EMAIL: " + email + " EXISTS");
    }
}
