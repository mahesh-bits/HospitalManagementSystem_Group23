package com.ss.notification.service.events;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private String type; // SMS / EMAIL
    private String eventType; // APPOINTMENT_CREATED, BILL_GENERATED
    private Long referenceId; // appointmentId or billId
    private String recipient; // phone/email
    private String message;
    private LocalDateTime eventTime;
}
