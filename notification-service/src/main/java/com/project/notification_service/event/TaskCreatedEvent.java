package com.project.notification_service.event;

import java.util.UUID;

public record TaskCreatedEvent(
        UUID eventId,
        Long taskId,
        Long groupId,
        String title,
        String createdBy
) implements TaskEvent {
    @Override
    public String eventType() {
        return "TaskCreated";
    }
}
