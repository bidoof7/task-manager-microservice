package com.project.auth_service.service;

import com.project.auth_service.entity.Account;
import com.project.auth_service.entity.OutboxEvent;
import com.project.auth_service.enums.OutboxStatus;
import com.project.auth_service.event.AccountCreatedEvent;
import com.project.auth_service.repository.OutboxEventRepository;
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

    public void saveAccountCreatedEvent(Account account) {
        try {
            UUID eventId = UUID.randomUUID();
            String eventType = "AccountCreated";

            AccountCreatedEvent event = new AccountCreatedEvent(
                    eventId,
                    eventType,
                    account.getId(),
                    account.getEmail(),
                    account.getStatus(),
                    account.getCreatedAt().toString()
            );

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = new OutboxEvent();

            outboxEvent.setAggregateType("Account");
            outboxEvent.setAggregateId(account.getId().toString());
            outboxEvent.setEventId(eventId);
            outboxEvent.setEventType(eventType);
            outboxEvent.setPayload(payload);
            outboxEvent.setStatus(OutboxStatus.PENDING);
            outboxEvent.setCreatedAt(LocalDateTime.now());

            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize AccountCreatedEvent", e);
        }
    }
}
