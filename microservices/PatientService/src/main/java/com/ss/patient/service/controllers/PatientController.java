////package com.ss.patient.service.controllers;
////
////import com.ss.patient.service.entities.Patient;
////import com.ss.patient.service.services.PatientService;
////import io.swagger.v3.oas.annotations.Operation;
////import io.swagger.v3.oas.annotations.tags.Tag;
////import lombok.RequiredArgsConstructor;
////import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
////import org.springframework.data.domain.Page;
////import org.springframework.data.domain.PageRequest;
////import org.springframework.data.domain.Pageable;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@CrossOrigin(origins = "*")
////@RestController
////@RequestMapping("/api/v1/patients")
////@RequiredArgsConstructor
////@Tag(name = "Patient API", description = "CRUD operations with pagination & filtering")
////public class PatientController {
////
////    private final PatientService patientService;
////    private final Logger logger = LoggerFactory.getLogger(PatientController.class);
////
////    @Operation(summary = "Create a new patient")
////    @PostMapping
////    public Patient createPatient(@RequestBody Patient patient) {
////        logPII(patient);
////        return patientService.createPatient(patient);
////    }
////
////    @Operation(summary = "Get patient by ID")
////    @GetMapping("/{id}")
////    public Patient getPatientById(@PathVariable Long id) {
////        return patientService.getPatientById(id)
////                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
////    }
////
////    @Operation(summary = "Get all patients with pagination")
////    @GetMapping
////    public Page<Patient> getAllPatients(
////            @RequestParam(defaultValue = "0") int page,
////            @RequestParam(defaultValue = "10") int size,
////            @RequestParam(required = false) String name,
////            @RequestParam(required = false) String phone
////    ) {
////        Pageable pageable = PageRequest.of(page, size);
////        return patientService.getPatientsWithFilter(name, phone, pageable);
////    }
////
////    @Operation(summary = "Update a patient by ID")
////    @PutMapping("/{id}")
////    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
////        logPII(patient);
////        return patientService.updatePatient(id, patient);
////    }
////
////    @Operation(summary = "Delete a patient by ID")
////    @DeleteMapping("/{id}")
////    public void deletePatient(@PathVariable Long id) {
////        patientService.deletePatient(id);
////    }
////
////    private void logPII(Patient patient) {
////        String maskedEmail = maskEmail(patient.getEmail());
////        String maskedPhone = maskPhone(patient.getPhone());
////        logger.info("Processing patient - Name: {}, Email: {}, Phone: {}",
////                patient.getName(), maskedEmail, maskedPhone);
////    }
////
////    private String maskEmail(String email) {
////        if (email == null || !email.contains("@")) return "****";
////        String[] parts = email.split("@");
////        String namePart = parts[0];
////        if (namePart.length() <= 2) namePart = "**";
////        else namePart = namePart.substring(0, 1) + "***" + namePart.substring(namePart.length() - 1);
////        return namePart + "@" + parts[1];
////    }
////
////    private String maskPhone(String phone) {
////        if (phone == null || phone.length() < 4) return "****";
////        return "****" + phone.substring(phone.length() - 4);
////    }
////}
//
//
//package com.ss.patient.service.entities;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//import java.time.LocalDate;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Table(name = "patients")
//public class Patient {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long patientId;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false, unique = true)
//    private String phone;
//
//    private LocalDate dob;
//
//    @Column(nullable = false)
//    private boolean active = true; // ✅ new field to check if patient is active
//
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    public void prePersist() {
//        createdAt = LocalDateTime.now();
//        if (this.active == false) this.active = true; // default to active
//    }
//}


package com.ss.patient.service.controllers;

import com.ss.patient.service.entities.Patient;
import com.ss.patient.service.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patient API", description = "CRUD operations with pagination & filtering")
public class PatientController {

    private final PatientService patientService;
    private final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Operation(summary = "Create a new patient")
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        logPII(patient);
        return patientService.createPatient(patient);
    }

    @Operation(summary = "Get patient by ID")
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    @Operation(summary = "Get all patients with pagination and filters")
    @GetMapping
    public Page<Patient> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean active
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return patientService.getPatientsWithFilter(name, phone, active, pageable);
    }

    @Operation(summary = "Update a patient by ID")
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        logPII(patient);
        return patientService.updatePatient(id, patient);
    }

    @Operation(summary = "Delete a patient by ID")
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @Operation(summary = "Activate or deactivate a patient")
    @PatchMapping("/{id}/status")
    public Patient updatePatientStatus(@PathVariable Long id, @RequestParam boolean active) {
        return patientService.toggleActiveStatus(id, active);
    }

    @Operation(summary = "Validate if a patient exists and is active (for Appointment Service)")
    @GetMapping("/{id}/validate")
    public Patient validatePatient(@PathVariable Long id) {
        return patientService.getActivePatient(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient is inactive or not found with id " + id));
    }

    private void logPII(Patient patient) {
        String maskedEmail = maskEmail(patient.getEmail());
        String maskedPhone = maskPhone(patient.getPhone());
        logger.info("Processing patient - Name: {}, Email: {}, Phone: {}",
                patient.getName(), maskedEmail, maskedPhone);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "****";
        String[] parts = email.split("@");
        String namePart = parts[0];
        if (namePart.length() <= 2) namePart = "**";
        else namePart = namePart.substring(0, 1) + "***" + namePart.substring(namePart.length() - 1);
        return namePart + "@" + parts[1];
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return "****";
        return "****" + phone.substring(phone.length() - 4);
    }
}
