package controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.KafkaConsumerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final KafkaConsumerService consumerService;

    @GetMapping("/consumed")
    public ResponseEntity<Map<String, Object>> getConsumedMessages() {
        List<String> messages = consumerService.getConsumedMessages();
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", messages.size());
        response.put("messages", messages);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getMessageCount() {
        Map<String, Object> response = new HashMap<>();
        response.put("count", consumerService.getMessageCount());
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearMessages() {
        consumerService.clearMessages();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "All messages cleared");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "kafka-consumer-dev");
        status.put("messagesConsumed", String.valueOf(consumerService.getMessageCount()));
        return ResponseEntity.ok(status);
    }
}