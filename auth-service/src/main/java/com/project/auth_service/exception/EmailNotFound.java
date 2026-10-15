package com.project.auth_service.exception;

public class EmailNotFound extends RuntimeException {
    public EmailNotFound(String email) {
        super("EMAIL " + email + " NOT FOUND");
    }
}
