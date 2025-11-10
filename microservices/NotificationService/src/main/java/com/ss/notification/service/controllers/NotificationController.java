package com.ss.notification.service.controllers;

import com.ss.notification.service.entities.Notification;
import com.ss.notification.service.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification APIs", description = "APIs to send SMS/Email reminders and view logs")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Send SMS/Email notification")
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.sendNotification(notification));
    }

    @GetMapping
    @Operation(summary = "List notifications with optional filters")
    public ResponseEntity<Page<Notification>> listNotifications(
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(notificationService.filterNotifications(recipient, type, status, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
}
