package com.project.notification_service.exception;

public class DuplicateEventException extends RuntimeException {
    public DuplicateEventException(String message) {
        super("EVENT ALREADY PROCESSED: " + message);
    }
}
