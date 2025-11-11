package com.ss.billing.service.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status; // GENERATED, PAID, CANCELLED

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
