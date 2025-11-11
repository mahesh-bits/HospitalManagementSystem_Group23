package com.ss.payment.service.controllers;

import com.ss.payment.service.entities.Payment;
import com.ss.payment.service.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment APIs", description = "APIs to capture payments and refunds")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    @Operation(summary = "List payments with optional filters")
    public ResponseEntity<Page<Payment>> listPayments(
            @RequestParam(required = false) Long billId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(paymentService.listPayments(billId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @PostMapping("/capture")
    @Operation(summary = "Capture a payment (idempotent)")
    public ResponseEntity<Payment> capturePayment(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.capturePayment(payment));
    }

    @PostMapping("/refund")
    @Operation(summary = "Capture a refund")
    public ResponseEntity<Payment> captureRefund(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.captureRefund(payment));
    }
}
