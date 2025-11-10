package com.ss.notification.service.services;

import com.ss.notification.service.entities.Notification;
import com.ss.notification.service.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    // Send a notification (mock SMS/Email)
    public Notification sendNotification(Notification notification) {
        try {
            // TODO: integrate with actual SMS/Email provider
            if ("EMAIL".equalsIgnoreCase(notification.getType())) {
                // --- Email sending logic ---
                if (mailSender != null) {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(notification.getRecipient());
                    message.setSubject("Notification - Reference ID: " + notification.getReferenceId());
                    message.setText(notification.getMessage());
                    message.setFrom("noreply@system.com");

                    mailSender.send(message);
                    notification.setStatus("SENT");
                } else {
                    // MailSender not configured
                    notification.setStatus("FAILED");
                }
            } else if ("SMS".equalsIgnoreCase(notification.getType())) {
                // --- Placeholder for future SMS integration ---
                // TODO: integrate with SMS provider (e.g., Twilio)
                notification.setStatus("SENT");
            } else {
                notification.setStatus("FAILED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            notification.setStatus("FAILED");
        }

        return notificationRepository.save(notification);
    }

    public Page<Notification> listNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id " + id));
    }

    public Page<Notification> filterNotifications(String recipient, String type, String status, Pageable pageable) {
        if (recipient != null) {
            return notificationRepository.findByRecipient(recipient, pageable);
        } else if (type != null) {
            return notificationRepository.findByType(type, pageable);
        } else if (status != null) {
            return notificationRepository.findByStatus(status, pageable);
        } else {
            return notificationRepository.findAll(pageable);
        }
    }
}
