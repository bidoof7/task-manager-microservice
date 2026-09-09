package com.project.notification_service.event;

import java.util.UUID;

public record TaskDeletedEvent(
        UUID eventId,
        Long taskId,
        String title
) implements TaskEvent {
    @Override
    public String eventType() { return "TaskDeleted"; }
}
