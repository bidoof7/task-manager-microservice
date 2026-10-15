package com.project.email_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic taskEventDltTopic() {
        return new NewTopic(
                "account-events.DLT",
                1,
                (short) 1
        );
    }
}
