package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KafkaConsumerService {

    private final List<String> consumedMessages = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(
            @Payload String message,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {
        
        log.info("=================================================");
        log.info("Consumed message from topic: {}", topic);
        log.info("Key: {}", key);
        log.info("Message: {}", message);
        log.info("Partition: {}", partition);
        log.info("Offset: {}", offset);
        log.info("Timestamp: {}", timestamp);
        log.info("=================================================");
        
        // Store message for retrieval via API
        consumedMessages.add(message);
        
        // Keep only last 100 messages
        if (consumedMessages.size() > 100) {
            consumedMessages.remove(0);
        }
    }

    public List<String> getConsumedMessages() {
        return new ArrayList<>(consumedMessages);
    }

    public int getMessageCount() {
        return consumedMessages.size();
    }

    public void clearMessages() {
        consumedMessages.clear();
        log.info("Cleared all consumed messages");
    }
}