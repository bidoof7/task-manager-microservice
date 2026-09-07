package com.project.task_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${kafka.bootstrap-servers}") String bootstrapServer;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(
                "bootstrap.servers",
                bootstrapServer
        );

        config.put(
                "key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"
        );

        config.put(
                "value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer"
        );

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic taskEventsTopic() {
        return new NewTopic(
                "task-events",
                3,
                (short) 1
        );
    }
}
