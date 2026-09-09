package com.project.notification_service.handlers;

import com.project.notification_service.event.TaskDeletedEvent;
import com.project.notification_service.event.TaskEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskDeletedHandler implements TaskEventHandler{
    @Override
    public String eventType() {
        return "TaskDeleted";
    }

    @Override
    public void handle(TaskEvent event) {
        TaskDeletedEvent deletedEvent = (TaskDeletedEvent) event;

        log.info(
                "Handle TaskDeleted: taskId={}, title={}",
                deletedEvent.taskId(),
                deletedEvent.title()
        );
    }
}
