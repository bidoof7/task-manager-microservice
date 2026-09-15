package com.project.task_service.event;

import java.util.UUID;

public record TaskCreatedEvent(
        UUID eventId,
        String eventType,
        Long taskId,
        Long groupId,
        String title,
        String createdBy
) {
}
