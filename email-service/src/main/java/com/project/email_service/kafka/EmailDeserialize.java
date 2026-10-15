package com.project.email_service.kafka;

import com.project.email_service.event.AccountCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class EmailDeserialize {
    private final ObjectMapper objectMapper;

    public AccountCreatedEvent deserialize(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);

            String eventType = root.get("eventType").asString();

            if (!eventType.equals("AccountCreated")) {
                throw new IllegalArgumentException("Fail to deserialize event type: " + eventType);
            }
            return objectMapper.treeToValue(root, AccountCreatedEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fail to deserialize " + e);
        }
    }
}
