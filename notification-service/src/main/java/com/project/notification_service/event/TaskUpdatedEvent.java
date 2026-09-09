package com.project.notification_service.event;

import java.util.UUID;

public record TaskUpdatedEvent(
        UUID eventId,
        Long taskId,
        String title,
        String createdBy
) implements TaskEvent {
    @Override
    public String eventType() {
        return "TaskUpdated";
    }
}
