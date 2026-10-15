package com.project.auth_service.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("REFRESH TOKEN INVALID");
    }
}
