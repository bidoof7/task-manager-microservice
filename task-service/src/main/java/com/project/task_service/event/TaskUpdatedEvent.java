package com.project.task_service.event;

import java.util.UUID;

public record TaskUpdatedEvent(
        UUID eventId,
        String eventType,
        Long taskId,
        String title,
        String updatedBy
) {
}
