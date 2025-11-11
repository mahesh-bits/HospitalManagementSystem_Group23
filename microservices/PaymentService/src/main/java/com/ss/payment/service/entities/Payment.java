package com.ss.payment.service.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private Long billId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String method; // CARD, UPI, CASH, NETBANKING etc.

    @Column(nullable = false, unique = true)
    private String reference; // unique reference for idempotency

    @Column(nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @PrePersist
    public void prePersist() {
        paidAt = LocalDateTime.now();
    }
}
