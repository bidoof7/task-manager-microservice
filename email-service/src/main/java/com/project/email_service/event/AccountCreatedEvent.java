package com.project.email_service.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccountCreatedEvent(
        UUID eventId,
        String eventType,
        Long accountId,
        String email,
        String status,
        LocalDateTime createdAt
) {
}
