package com.ss.notification.service.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String type; // SMS, EMAIL

    @Column(nullable = false)
    private Long referenceId; // appointmentId or billId

    @Column(nullable = false)
    private String recipient; // email or phone

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String status; // SENT, FAILED

    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt;

    @PrePersist
    public void prePersist() {
        sentAt = LocalDateTime.now();
    }
}
