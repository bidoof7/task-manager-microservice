package com.project.notification_service.service;

import com.project.notification_service.entity.ProcessedEvent;
import com.project.notification_service.event.TaskEvent;
import com.project.notification_service.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventProcessService {
    private final ProcessedEventRepository processedEventRepository;

    public boolean alreadyProcessed(TaskEvent event) {
       return processedEventRepository.existsById(event.eventId());
    }

    public void markAsProcessed(TaskEvent event) {
        ProcessedEvent processedEvent = new ProcessedEvent();

        processedEvent.setEventId(event.eventId());
        processedEvent.setEventType(event.eventType());
        processedEvent.setProcessedAt(LocalDateTime.now());

        processedEventRepository.save(processedEvent);
    }
}
