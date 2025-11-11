package com.ss.billing.service.services;

import com.ss.billing.service.entities.Bill;
import com.ss.billing.service.repositories.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepository;

    private static final double TAX_PERCENTAGE = 0.18; // 18% GST for example

    // Generate bill for completed appointments
    public Bill generateBill(Long patientId, Long appointmentId, double baseAmount) {
        double totalAmount = computeTaxes(baseAmount);
        Bill bill = Bill.builder()
                .patientId(patientId)
                .appointmentId(appointmentId)
                .amount(totalAmount)
                .status("GENERATED")
                .build();
        return billRepository.save(bill);
    }

    // Compute taxes
    private double computeTaxes(double baseAmount) {
        return baseAmount + (baseAmount * TAX_PERCENTAGE);
    }

    // Handle cancellation
    public Bill cancelBill(Long billId) {
        Bill bill = getBillById(billId);
        bill.setStatus("CANCELLED");
        return billRepository.save(bill);
    }

    public Bill getBillById(Long billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id " + billId));
    }

    public Page<Bill> getBills(Long patientId, String status, Pageable pageable) {
        if (patientId != null) {
            return billRepository.findByPatientId(patientId, pageable);
        } else if (status != null && !status.isBlank()) {
            return billRepository.findByStatusContainingIgnoreCase(status, pageable);
        } else {
            return billRepository.findAll(pageable);
        }
    }
}
