//package com.ss.patient.service.repositories;
//
//import com.ss.patient.service.entities.Patient;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface PatientRepository extends JpaRepository<Patient, Long> {
//    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);
//    Page<Patient> findByPhoneContaining(String phone, Pageable pageable);
//
////    Optional<Patient> findByIdAndStatus(Long id, String status);
//
//}
//

package com.ss.patient.service.repositories;

import com.ss.patient.service.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Patient> findByPhoneContaining(String phone, Pageable pageable);

    // ✅ New methods
    Page<Patient> findByActive(boolean active, Pageable pageable);
    Optional<Patient> findByPatientIdAndActiveTrue(Long patientId);
}

