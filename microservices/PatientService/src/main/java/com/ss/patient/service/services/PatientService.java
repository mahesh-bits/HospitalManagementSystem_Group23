//package com.ss.patient.service.services;
//
//import com.ss.patient.service.entities.Patient;
//import com.ss.patient.service.repositories.PatientRepository;
//import com.ss.patient.service.controllers.ResourceNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PatientService {
//
//    private final PatientRepository patientRepository;
//
//    // Create patient
//    public Patient createPatient(Patient patient) {
//        return patientRepository.save(patient);
//    }
//
//    // Get patient by ID
//    public Optional<Patient> getPatientById(Long id) {
//        return patientRepository.findById(id);
//    }
//
////    public Optional<Patient> getActivePatient(Long patientId) {
////        return patientRepository.findByIdAndStatus(id, "ACTIVE");
////    }
//
//    // Update patient
//    public Patient updatePatient(Long id, Patient updatedPatient) {
//        return patientRepository.findById(id)
//                .map(patient -> {
//                    patient.setName(updatedPatient.getName());
//                    patient.setEmail(updatedPatient.getEmail());
//                    patient.setPhone(updatedPatient.getPhone());
//                    patient.setDob(updatedPatient.getDob());
//                    return patientRepository.save(patient);
//                })
//                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
//    }
//
//    // Delete patient
//    public void deletePatient(Long id) {
//        if (!patientRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Patient not found with id " + id);
//        }
//        patientRepository.deleteById(id);
//    }
//
//    // Get patients with pagination and optional filtering by name or phone
//    public Page<Patient> getPatientsWithFilter(String name, String phone, Pageable pageable) {
//        if (name != null && !name.isEmpty()) {
//            return patientRepository.findByNameContainingIgnoreCase(name, pageable);
//        }
//        if (phone != null && !phone.isEmpty()) {
//            return patientRepository.findByPhoneContaining(phone, pageable);
//        }
//        return patientRepository.findAll(pageable);
//    }
//}


//package com.ss.patient.service.services;
//
//import com.ss.patient.service.entities.Patient;
//import com.ss.patient.service.repositories.PatientRepository;
//import com.ss.patient.service.controllers.ResourceNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PatientService {
//
//    private final PatientRepository patientRepository;
//
//    // Create patient
//    public Patient createPatient(Patient patient) {
//        if (patient.getActive() == false) {
//            patient.setActive(true);
//        }
//        return patientRepository.save(patient);
//    }
//
//    // Get patient by ID
//    public Optional<Patient> getPatientById(Long id) {
//        return patientRepository.findById(id);
//    }
//
//    // ✅ Get active patient (for Appointment validation)
//    public Optional<Patient> getActivePatient(Long id) {
//        return patientRepository.findByPatientIdAndActiveTrue(id);
//    }
//
//    // ✅ Toggle active/inactive
//    public Patient toggleActiveStatus(Long id, boolean active) {
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
//        patient.setActive(active);
//        return patientRepository.save(patient);
//    }
//
//    // Update patient
//    public Patient updatePatient(Long id, Patient updatedPatient) {
//        return patientRepository.findById(id)
//                .map(patient -> {
//                    patient.setName(updatedPatient.getName());
//                    patient.setEmail(updatedPatient.getEmail());
//                    patient.setPhone(updatedPatient.getPhone());
//                    patient.setDob(updatedPatient.getDob());
//                    patient.setActive(updatedPatient.getActive());
//                    return patientRepository.save(patient);
//                })
//                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
//    }
//
//    // Delete patient
//    public void deletePatient(Long id) {
//        if (!patientRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Patient not found with id " + id);
//        }
//        patientRepository.deleteById(id);
//    }
//
//    // Get patients with optional filtering by name, phone, or active
//    public Page<Patient> getPatientsWithFilter(String name, String phone, Boolean active, Pageable pageable) {
//        if (name != null && !name.isEmpty()) {
//            return patientRepository.findByNameContainingIgnoreCase(name, pageable);
//        }
//        if (phone != null && !phone.isEmpty()) {
//            return patientRepository.findByPhoneContaining(phone, pageable);
//        }
//        if (active != null) {
//            return patientRepository.findByActive(active, pageable);
//        }
//        return patientRepository.findAll(pageable);
//    }
//}


package com.ss.patient.service.services;

import com.ss.patient.service.entities.Patient;
import com.ss.patient.service.repositories.PatientRepository;
import com.ss.patient.service.controllers.ResourceNotFoundException;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final MeterRegistry meterRegistry; // ✅ Micrometer registry

    // ✅ Create patient
    public Patient createPatient(Patient patient) {
        if (patient.getActive() == false) {
            patient.setActive(true);
        }

        Patient savedPatient = patientRepository.save(patient);

        // Increment custom metric
        meterRegistry.counter("patient.created.count").increment();

        return savedPatient;
    }

    // ✅ Get patient by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // ✅ Get only active patients (used by Appointment Service)
    public Optional<Patient> getActivePatient(Long id) {
        return patientRepository.findByPatientIdAndActiveTrue(id);
    }

    // ✅ Toggle active/inactive
    public Patient toggleActiveStatus(Long id, boolean active) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
        patient.setActive(active);
        return patientRepository.save(patient);
    }

    // ✅ Update patient
    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setName(updatedPatient.getName());
                    patient.setEmail(updatedPatient.getEmail());
                    patient.setPhone(updatedPatient.getPhone());
                    patient.setDob(updatedPatient.getDob());
                    patient.setActive(updatedPatient.getActive());
                    Patient saved = patientRepository.save(patient);

                    // Increment custom metric
                    meterRegistry.counter("patient.updated.count").increment();

                    return saved;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    // ✅ Delete patient
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id " + id);
        }
        patientRepository.deleteById(id);

        // Increment custom metric
        meterRegistry.counter("patient.deleted.count").increment();
    }

    // ✅ Get patients with filtering
    public Page<Patient> getPatientsWithFilter(String name, String phone, Boolean active, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return patientRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        if (phone != null && !phone.isEmpty()) {
            return patientRepository.findByPhoneContaining(phone, pageable);
        }
        if (active != null) {
            return patientRepository.findByActive(active, pageable);
        }
        return patientRepository.findAll(pageable);
    }
}
