package com.ss.payment.service.services;

import com.ss.payment.service.entities.Payment;
import com.ss.payment.service.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // Capture payment (idempotent using reference)
    public Payment capturePayment(Payment payment) {
        paymentRepository.findByReference(payment.getReference())
                .ifPresent(existing -> {
                    throw new RuntimeException("Payment with reference already exists: " + payment.getReference());
                });
        return paymentRepository.save(payment);
    }

    // Capture refund (negative amount)
    public Payment captureRefund(Payment payment) {
        payment.setAmount(-Math.abs(payment.getAmount())); // Refund as negative
        return capturePayment(payment);
    }

    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + paymentId));
    }

    public Page<Payment> listPayments(Long billId, Pageable pageable) {
        if (billId != null) {
            return paymentRepository.findByBillId(billId, pageable);
        } else {
            return paymentRepository.findAll(pageable);
        }
    }
}
