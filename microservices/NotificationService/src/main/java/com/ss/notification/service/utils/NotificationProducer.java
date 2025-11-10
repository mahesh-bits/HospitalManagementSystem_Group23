package com.ss.notification.service.utils;

import com.ss.notification.service.events.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private static final String TOPIC = "notifications";

    public NotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotificationEvent(NotificationEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
