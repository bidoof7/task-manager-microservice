package com.project.task_service.outbox;

import com.project.task_service.entity.OutboxEvent;
import com.project.task_service.enums.OutboxStatus;
import com.project.task_service.repository.OutboxEventRepository;
import com.project.task_service.service.OutboxClaimService;
import com.project.task_service.service.OutboxRecoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private static final String TOPIC = "task-events";

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxClaimService outboxClaimService;
    private final OutboxRecoveryService outboxRecoveryService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {
        List<OutboxEvent> events = outboxClaimService.claimEvent();

        for (OutboxEvent event : events) {
            publish(event);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void recoverStuckEvent() {
        outboxRecoveryService.recoverStuckEvent();
    }

    public void publish(OutboxEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.getAggregateId(), event.getPayload()).get();

            event.setStatus(OutboxStatus.PUBLISHED);
            event.setPublishedAt(LocalDateTime.now());

            outboxEventRepository.save(event);

            log.info(
                    "Outbox event publised: id={}, eventId={}, type={}, aggregateId={}",
                    event.getId(),
                    event.getEventId(),
                    event.getEventType(),
                    event.getAggregateId()
            );
        } catch (Exception e) {
            event.setLastError(truncateError(e.getMessage()));

            outboxEventRepository.save(event);

            log.error(
                    "Failed to publish outbox event: id={}",
                    event.getId(),
                    e
            );
        }
    }
    private String truncateError(String message) {

        if (message == null) {
            return null;
        }

        return message.length() <= 2000
                ? message
                : message.substring(0, 2000);
    }
}
