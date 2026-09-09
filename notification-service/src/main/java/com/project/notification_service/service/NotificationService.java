package com.project.notification_service.service;

import com.project.notification_service.event.TaskEvent;
import com.project.notification_service.handlers.EventHandlerRegistry;
import com.project.notification_service.handlers.TaskEventHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EventProcessService eventProcessService;
    private final EventHandlerRegistry eventHandlerRegistry;

    @Transactional
    public void process(TaskEvent event) {
        if (eventProcessService.alreadyProcessed(event)) {
            log.info("Event already processed at: eventId={}",
                    event.eventId());
            return;
        }

        log.info(
                "Processing TaskCreated: taskId={}, title={}, createdBy={}",
                event.taskId(),
                event.eventId(),
                event.eventType()
        );

        TaskEventHandler handler = eventHandlerRegistry.getHandler(event.eventType());

        handler.handle(event);

        eventProcessService.markAsProcessed(event);
    }
}
