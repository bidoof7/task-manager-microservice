package com.project.auth_service.service;

import com.project.auth_service.entity.OutboxEvent;
import com.project.auth_service.enums.OutboxStatus;
import com.project.auth_service.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxRecoveryService {
    private static final long CLAIM_TIMEOUT_MIN = 5;

    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void recoverStuckEvent() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(CLAIM_TIMEOUT_MIN);

        List<OutboxEvent> events = outboxEventRepository.findStuckEvents(threshold);

        for (OutboxEvent event : events) {
            int retryCount = event.getRetryCount() == null ? 0 : event.getRetryCount();

            retryCount++;

            event.setRetryCount(retryCount);
            event.setStatus(OutboxStatus.PENDING);
            event.setClaimedAt(null);

            event.setNextRetryAt(calculateNextRetryTime(now, retryCount));
        }

        outboxEventRepository.saveAll(events);
    }

    private LocalDateTime calculateNextRetryTime(LocalDateTime now, int retryCount) {
        long delaySeconds =
                switch (retryCount) {
                    case 1 -> 5;
                    case 2 -> 30;
                    case 3 -> 60;
                    case 4 -> 300;
                    default -> 900;
                };
        return now.plusSeconds(delaySeconds);
    }
}
