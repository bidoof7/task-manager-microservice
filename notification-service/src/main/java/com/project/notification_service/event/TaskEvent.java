package com.project.notification_service.event;

import java.util.UUID;

public interface TaskEvent {
    UUID eventId();
    Long taskId();
    String eventType();
}
