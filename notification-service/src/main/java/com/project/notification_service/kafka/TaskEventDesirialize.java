package com.project.notification_service.kafka;

import com.project.notification_service.event.TaskCreatedEvent;
import com.project.notification_service.event.TaskDeletedEvent;
import com.project.notification_service.event.TaskEvent;
import com.project.notification_service.event.TaskUpdatedEvent;
import com.project.notification_service.handlers.EventHandlerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class TaskEventDesirialize {
    private final ObjectMapper objectMapper;

    public TaskEvent desirialize(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);

            String eventType = root.get("eventType").asString();

            return switch (eventType) {
                case "TaskCreated" -> objectMapper.treeToValue(root, TaskCreatedEvent.class);

                case "TaskUpdated" -> objectMapper.treeToValue(root, TaskUpdatedEvent.class);

                case "TaskDeleted" -> objectMapper.treeToValue(root, TaskDeletedEvent.class);

                default -> throw new IllegalArgumentException("Unknown event type: " + eventType);

            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to desiralize", e);
        }
    }
}
