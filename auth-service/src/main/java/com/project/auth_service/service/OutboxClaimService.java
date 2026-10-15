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
public class OutboxClaimService {
    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public List<OutboxEvent> claimEvent() {
        LocalDateTime now = LocalDateTime.now();

        List<OutboxEvent> events = outboxEventRepository.findPendingForUpdate(now);

        for (OutboxEvent event : events) {
            event.setStatus(OutboxStatus.PROCESSING);
            event.setClaimedAt(now);
        }

        outboxEventRepository.saveAll(events);

        return events;
    }
}
