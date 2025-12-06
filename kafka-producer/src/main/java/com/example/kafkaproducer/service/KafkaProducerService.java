package com.example.kafkaproducer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public void sendMessage(String key, String message) {
        log.info("Sending message to topic: {} with key: {}", topicName, key);
        
        CompletableFuture<SendResult<String, String>> future = 
            kafkaTemplate.send(topicName, key, message);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent successfully: key={}, offset={}, partition={}", 
                    key,
                    result.getRecordMetadata().offset(),
                    result.getRecordMetadata().partition());
            } else {
                log.error("Failed to send message: key={}, error={}", key, ex.getMessage());
            }
        });
    }

    public void sendMessage(String message) {
        sendMessage(null, message);
    }
}