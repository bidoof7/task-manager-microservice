package com.project.notification_service.handlers;

import com.project.notification_service.event.TaskEvent;

public interface TaskEventHandler {
    String eventType();
    void handle(TaskEvent event);
}
