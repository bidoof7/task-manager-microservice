package com.project.notification_service.kafka;

import com.project.notification_service.event.TaskEvent;
import com.project.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventConsumer {
    private final NotificationService notificationService;
    private final TaskEventDesirialize taskEventDesirialize;

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
            TaskEvent event = taskEventDesirialize.desirialize(message);

            notificationService.process(event);

            log.info(
                    "Kafka message processed successfully: eventType={}, partition={}, offset={}",
                    event.eventType(),
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
