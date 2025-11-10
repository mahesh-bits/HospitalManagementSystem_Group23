package com.ss.appointment.service.controllers;

import com.ss.appointment.service.entities.Appointment;
import com.ss.appointment.service.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointment APIs", description = "APIs to book, reschedule, cancel appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "List appointments with optional filters")
    public ResponseEntity<Page<Appointment>> listAppointments(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String department,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointments(patientId, doctorId, department, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping
    @Operation(summary = "Book a new appointment")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
        //Verify patient is

        return ResponseEntity.ok(appointmentService.bookAppointment(appointment));
    }

    @PutMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule an existing appointment")
    public ResponseEntity<Appointment> rescheduleAppointment(@PathVariable Long id,
                                                             @RequestParam LocalDateTime slotStart,
                                                             @RequestParam LocalDateTime slotEnd) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, slotStart, slotEnd));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
