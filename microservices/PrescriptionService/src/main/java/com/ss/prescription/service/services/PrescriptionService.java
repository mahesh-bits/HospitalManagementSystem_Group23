package com.ss.prescription.service.services;

import com.ss.prescription.service.entities.Prescription;
import com.ss.prescription.service.repositories.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    // Create prescription
    public Prescription createPrescription(Prescription prescription) {
        // Ensure appointment, patient, doctor IDs exist (can call AppointmentService API in a real scenario)
        return prescriptionRepository.save(prescription);
    }

    // Get prescription by ID
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found with id " + id));
    }

    // List prescriptions with filters
    public Page<Prescription> listPrescriptions(Long patientId, Long doctorId, Long appointmentId, Pageable pageable) {
        if (patientId != null) {
            return prescriptionRepository.findByPatientId(patientId, pageable);
        } else if (doctorId != null) {
            return prescriptionRepository.findByDoctorId(doctorId, pageable);
        } else if (appointmentId != null) {
            return prescriptionRepository.findByAppointmentId(appointmentId, pageable);
        } else {
            return prescriptionRepository.findAll(pageable);
        }
    }
}
