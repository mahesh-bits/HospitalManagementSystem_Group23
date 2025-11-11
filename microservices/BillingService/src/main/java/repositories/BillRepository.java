package com.ss.billing.service.repositories;

import com.ss.billing.service.entities.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findByPatientId(Long patientId, Pageable pageable);

    Page<Bill> findByStatusContainingIgnoreCase(String status, Pageable pageable);
}
