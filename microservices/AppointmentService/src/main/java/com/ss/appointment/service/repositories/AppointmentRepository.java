package com.ss.appointment.service.repositories;

import com.ss.appointment.service.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);

    Page<Appointment> findByDepartmentContainingIgnoreCase(String department, Pageable pageable);

    // Slot collision check
    List<Appointment> findByDoctorIdAndSlotStartLessThanAndSlotEndGreaterThanAndStatus(
            Long doctorId, LocalDateTime end, LocalDateTime start, String status);
}
