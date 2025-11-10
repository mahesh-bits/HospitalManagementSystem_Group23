package com.ss.notification.service.services;

import com.ss.notification.service.entities.Notification;
import com.ss.notification.service.repositories.NotificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationRepository repository;


    public EmailService(JavaMailSender mailSender, NotificationRepository repository) {
        this.mailSender = mailSender;
        this.repository = repository;
    }

    public Notification sendEmail(String to, String subject, String body) {
        // Using Spring Boot JavaMailSender
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        System.out.println("Email sent to " + to + ": " + body);

        // Build and return a Notification entity
        Notification notification = Notification.builder()
                .recipient(to)
                .type("EMAIL")
                .message(body)
                .sentAt(LocalDateTime.now())
                .build();

        // Optionally, save to repository if you want persistence
        return repository.save(notification);
    }

}
