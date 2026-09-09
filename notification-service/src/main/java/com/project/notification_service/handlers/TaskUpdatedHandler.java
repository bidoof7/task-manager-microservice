package com.project.notification_service.handlers;

import com.project.notification_service.event.TaskEvent;
import com.project.notification_service.event.TaskUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskUpdatedHandler implements TaskEventHandler{
    @Override
    public String eventType() {
        return "TaskUpdated";
    }

    @Override
    public void handle(TaskEvent event) {
        TaskUpdatedEvent updatedEvent = (TaskUpdatedEvent) event;

        log.info(
                "Handle TaskUpdated: taskId={}, title={}, createdBy={}",
                updatedEvent.taskId(),
                updatedEvent.title(),
                updatedEvent.createdBy()
        );

    }
}
