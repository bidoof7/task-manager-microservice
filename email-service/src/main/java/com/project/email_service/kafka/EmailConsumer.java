package com.project.email_service.kafka;

import com.project.email_service.event.AccountCreatedEvent;
import com.project.email_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final EmailDeserialize emailDeserialize;
    private final EmailService emailService;

    @KafkaListener(
            topics = "account-events",
            groupId = "email-service"
    )
    public void consume(String message) {
        AccountCreatedEvent event = emailDeserialize.deserialize(message);

        emailService.sendAccountCreatedEmail(event.email());
    }
}
