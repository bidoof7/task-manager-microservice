package com.project.notification_service.service;

import com.project.notification_service.entity.ProcessedEvent;
import com.project.notification_service.event.TaskCreatedEvent;
import com.project.notification_service.event.TaskUpdatedEvent;
import com.project.notification_service.exception.DuplicateEventException;
import com.project.notification_service.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public void processTaskCreated(TaskCreatedEvent event) {
        if(processedEventRepository.existsById(event.eventId())){
            log.info(
                    "Event already processed: eventId={}",
                    event.eventId()
            );
            return;
        }

        log.info(
                "Processing TaskCreated: taskId={}, title={}, createdBy={}",
                event.taskId(),
                event.title(),
                event.createdBy()
        );

        ProcessedEvent processedEvent = new ProcessedEvent();

        processedEvent.setEventId(event.eventId());
        processedEvent.setEventType("TaskCreated");
        processedEvent.setProcessedAt(LocalDateTime.now());

        processedEventRepository.save(processedEvent);
    }

    @Transactional
    public void processTaskUpdated(TaskUpdatedEvent event) {
        if(processedEventRepository.existsById(event.eventId())){
            log.info(
                    "Event already processed: eventId={}",
                    event.eventId()
            );
            return;
        }

        log.info(
                "Processing TaskUpdated: taskId={}, title={}, createdBy={}",
                event.taskId(),
                event.title(),
                event.createdBy()
        );

        ProcessedEvent processedEvent = new ProcessedEvent();

        processedEvent.setEventId(event.eventId());
        processedEvent.setEventType("TaskUpdated");
        processedEvent.setProcessedAt(LocalDateTime.now());

        processedEventRepository.save(processedEvent);
    }
}
