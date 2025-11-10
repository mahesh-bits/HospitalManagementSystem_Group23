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
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    public void prePersist() {
//        createdAt = LocalDateTime.now();
//    }
//}

package com.ss.patient.service.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    private LocalDate dob;

    @Column(nullable = false)
    private boolean active = true; // ✅ added field for active/inactive

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (!this.active) this.active = true; // ensure default true
    }

    public boolean getActive() {
        return active;
    }
}
