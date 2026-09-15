package com.project.notification_service.handlers;

import com.project.notification_service.event.TaskCreatedEvent;
import com.project.notification_service.event.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskCreatedHandler implements TaskEventHandler{
    @Override
    public String eventType() {
        return "TaskCreated";
    }

    @Override
    public void handle(TaskEvent event) {
        TaskCreatedEvent createdEvent = (TaskCreatedEvent) event;

        log.info(
                "Handle TaskCreated: taskId={}, groupId={}, title={}, createdBy={}",
                createdEvent.taskId(),
                createdEvent.groupId(),
                createdEvent.title(),
                createdEvent.createdBy()
        );
    }
}
