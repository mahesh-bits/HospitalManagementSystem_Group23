package com.ss.billing.service.controllers;

import com.ss.billing.service.entities.Bill;
import com.ss.billing.service.services.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bills")
@Tag(name = "Billing APIs", description = "APIs for generating and managing bills")
public class BillController {

    @Autowired
    private BillingService billingService;

    @GetMapping
    @Operation(summary = "List bills with optional filters")
    public ResponseEntity<Page<Bill>> listBills(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String status,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(billingService.getBills(patientId, status, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bill by ID")
    public ResponseEntity<Bill> getBill(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillById(id));
    }

    @PostMapping("/generate")
    @Operation(summary = "Generate bill for a completed appointment")
    public ResponseEntity<Bill> generateBill(@RequestParam Long patientId,
                                             @RequestParam Long appointmentId,
                                             @RequestParam double baseAmount) {
        return ResponseEntity.ok(billingService.generateBill(patientId, appointmentId, baseAmount));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a bill")
    public ResponseEntity<Bill> cancelBill(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.cancelBill(id));
    }
}
