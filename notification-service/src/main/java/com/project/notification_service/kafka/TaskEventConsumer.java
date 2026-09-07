package com.project.notification_service.kafka;

import com.project.notification_service.event.TaskCreatedEvent;
import com.project.notification_service.event.TaskUpdatedEvent;
import com.project.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventConsumer {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @KafkaListener(topics = "task-events", groupId = "notification-service", concurrency = "3")
    public void consume(String message, ConsumerRecord<String, String> record) {
        log.info(
                "Received Kafka message: topic={}, partition={}, offset={}, key={}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key()
        );

        try {
            JsonNode root = objectMapper.readTree(message);
            // DEBUG
            log.info("Kafka msg: {}", root);
            String eventType = root.get("eventType").asString();

            switch (eventType) {
                case "TaskCreated" -> {
                    TaskCreatedEvent event = objectMapper.treeToValue(root, TaskCreatedEvent.class);
                    notificationService.processTaskCreated(event);
                }
                case "TaskUpdated" -> {
                    TaskUpdatedEvent event = objectMapper.treeToValue(root, TaskUpdatedEvent.class);
                    notificationService.processTaskUpdated(event);
                }
                default -> {
                    log.warn(
                            "Unknown event type: {}",
                            eventType
                    );
                }
            }

            log.info(
                    "Kafka message processed successfully: eventType={}, partition={}, offset={}",
                    eventType,
                    record.partition(),
                    record.offset()
            );
        } catch (Exception e) {
            log.error(
                    "Failed to process TaskCreated event: {}",
                    message,
                    e
            );

            throw new RuntimeException(e);
        }
    }
}
