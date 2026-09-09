package com.project.task_service.event;

import java.util.UUID;

public record TaskDeletedEvent(
        UUID eventId,
        String eventType,
        Long taskId,
        String title
) {
}
