package com.example.kafkaproducer.controller;

import com.example.kafkaproducer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final KafkaProducerService producerService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @RequestParam(required = false) String key,
            @RequestBody String message) {
        
        log.info("Received request to send message");
        
        if (key != null && !key.isEmpty()) {
            producerService.sendMessage(key, message);
        } else {
            producerService.sendMessage(message);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Message sent to Kafka");
        response.put("message", message);
        response.put("key", key);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "kafka-producer-dev");
        return ResponseEntity.ok(status);
    }
}