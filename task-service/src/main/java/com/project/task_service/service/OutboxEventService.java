package com.project.task_service.service;

import com.project.task_service.entity.OutboxEvent;
import com.project.task_service.entity.Task;
import com.project.task_service.enums.OutboxStatus;
import com.project.task_service.enums.TaskEvent;
import com.project.task_service.event.TaskCreatedEvent;
import com.project.task_service.event.TaskUpdatedEvent;
import com.project.task_service.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void saveTaskCreatedEvent(Task task) {
        try {
            UUID eventId = UUID.randomUUID();
            String eventType = TaskEvent.TASK_CREATED.getTaskStatus();

            TaskCreatedEvent event = new TaskCreatedEvent(
                    eventId,
                    eventType,
                    task.getId(),
                    task.getTitle(),
                    task.getCreatedBy()
            );

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = new OutboxEvent();

            outboxEvent.setAggregateType("Task");
            outboxEvent.setAggregateId(task.getId().toString());
            outboxEvent.setEventId(eventId);
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(payload);
            outboxEvent.setStatus(OutboxStatus.PENDING);
            outboxEvent.setCreatedAt(LocalDateTime.now());

            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize TaskCreatedEvent", e);
        }
    }

    public void saveTaskUpdatedEvent(Task task) {
        try {
            UUID eventId = UUID.randomUUID();
            String eventType = TaskEvent.TASK_UPDATED.getTaskStatus();

            TaskUpdatedEvent event = new TaskUpdatedEvent(
                    eventId,
                    eventType,
                    task.getId(),
                    task.getTitle(),
                    task.getCreatedBy()
            );

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = new OutboxEvent();

            outboxEvent.setAggregateType("Task");
            outboxEvent.setAggregateId(task.getId().toString());
            outboxEvent.setEventId(eventId);
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(payload);
            outboxEvent.setStatus(OutboxStatus.PENDING);
            outboxEvent.setCreatedAt(LocalDateTime.now());

            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize TaskCreatedEvent", e);
        }
    }
}
