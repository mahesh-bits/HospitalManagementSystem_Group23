package com.ss.notification.service.services;

import com.ss.notification.service.entities.Notification;
import com.ss.notification.service.events.NotificationEvent;
import com.ss.notification.service.repositories.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {

    private final NotificationRepository notificationRepository;
    private final SmsService smsService;
    private final EmailService emailService;

    public NotificationConsumerService(NotificationRepository notificationRepository,
                                       SmsService smsService,
                                       EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.smsService = smsService;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "notifications", groupId = "notification-service")
    public void consume(NotificationEvent event) {
        Notification notification = Notification.builder()
                .type(event.getType())
                .referenceId(event.getReferenceId())
                .recipient(event.getRecipient())
                .message(event.getMessage())
                .status("PENDING")
                .build();

        try {
            if ("SMS".equalsIgnoreCase(event.getType())) {
                smsService.sendSms(event.getRecipient(), event.getMessage());
            } else if ("EMAIL".equalsIgnoreCase(event.getType())) {
                emailService.sendEmail(event.getRecipient(), "Notification", event.getMessage());
            }
            notification.setStatus("SENT");
        } catch (Exception e) {
            notification.setStatus("FAILED");
        }

        notificationRepository.save(notification);
        System.out.println("Notification processed: " + notification);
    }
}
