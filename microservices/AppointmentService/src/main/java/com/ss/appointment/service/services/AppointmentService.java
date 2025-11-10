package com.ss.appointment.service.services;

import com.ss.appointment.service.entities.Appointment;
import com.ss.appointment.service.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

//    @Value("${services.patient.url:http://localhost:8081/api/v1/patients}")
    @Value("${services.patient.url:http://patient-service:8081/api/v1/patients}")
    private String patientServiceUrl;

//    @Value("${services.doctor.url:http://localhost:8082/api/v1/doctors}")
    @Value("${services.doctor.url:http://doctor-service:8082/api/v1/doctors}")
    private String doctorServiceUrl;

//    @Value("${services.notification.url:http://localhost:8084/api/v1/notifications}")
    @Value("${services.notification.url:http://notification-service:8087/api/v1/notifications}")
    private String notificationServiceUrl;

    // BOOK APPOINTMENT
    public Appointment bookAppointment(Appointment appointment) {

        // Validate patient (must exist & active)
        try {
            restTemplate.getForObject(
                    patientServiceUrl + "/" + appointment.getPatientId() + "/validate",
                    Object.class
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid or inactive patient with ID " + appointment.getPatientId());
        }

        // Validate doctor (must exist, active, and in correct department)
        try {
            restTemplate.getForObject(
                    doctorServiceUrl + "/" + appointment.getDoctorId() + "/validate?department=" + appointment.getDepartment(),
                    Object.class
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid doctor or department mismatch for doctor ID " + appointment.getDoctorId());
        }

        // Check slot collision
        checkSlotCollision(appointment.getDoctorId(), appointment.getSlotStart(), appointment.getSlotEnd());

        // Save appointment
        appointment.setStatus("BOOKED");
        Appointment saved = appointmentRepository.save(appointment);

        // Notify confirmation (asynchronously in real-world)
        sendNotification(saved, "BOOKED", "Your appointment is confirmed for " + appointment.getSlotStart());

        return saved;
    }

    // RESCHEDULE APPOINTMENT
    public Appointment rescheduleAppointment(Long appointmentId, LocalDateTime newStart, LocalDateTime newEnd) {
        Appointment existing = getAppointmentById(appointmentId);
        checkSlotCollision(existing.getDoctorId(), newStart, newEnd, appointmentId);
        existing.setSlotStart(newStart);
        existing.setSlotEnd(newEnd);
        existing.setStatus("RESCHEDULED");
        Appointment updated = appointmentRepository.save(existing);

        sendNotification(updated, "RESCHEDULED",
                "Your appointment has been rescheduled to " + newStart);
        return updated;
    }

    // CANCEL APPOINTMENT
    public void cancelAppointment(Long appointmentId) {
        Appointment existing = getAppointmentById(appointmentId);
        existing.setStatus("CANCELLED");
        appointmentRepository.save(existing);

        sendNotification(existing, "CANCELLED",
                "Your appointment scheduled for " + existing.getSlotStart() + " has been cancelled.");
    }

    // COMPLETE APPOINTMENT
    public Appointment completeAppointment(Long appointmentId) {
        Appointment existing = getAppointmentById(appointmentId);
        existing.setStatus("COMPLETED");
        Appointment completed = appointmentRepository.save(existing);
        sendNotification(completed, "COMPLETED", "Your appointment has been completed successfully.");
        return completed;
    }

    // GET APPOINTMENT BY ID
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Appointment not found with id " + id));
    }

    // LIST APPOINTMENTS
    public Page<Appointment> getAppointments(Long patientId, Long doctorId, String department, Pageable pageable) {
        if (patientId != null) {
            return appointmentRepository.findByPatientId(patientId, pageable);
        } else if (doctorId != null) {
            return appointmentRepository.findByDoctorId(doctorId, pageable);
        } else if (department != null && !department.isBlank()) {
            return appointmentRepository.findByDepartmentContainingIgnoreCase(department, pageable);
        } else {
            return appointmentRepository.findAll(pageable);
        }
    }

    // SLOT COLLISION CHECK
    private void checkSlotCollision(Long doctorId, LocalDateTime start, LocalDateTime end) {
        checkSlotCollision(doctorId, start, end, null);
    }

    private void checkSlotCollision(Long doctorId, LocalDateTime start, LocalDateTime end, Long excludeAppointmentId) {
        List<Appointment> collisions = appointmentRepository.findByDoctorIdAndSlotStartLessThanAndSlotEndGreaterThanAndStatus(
                doctorId, end, start, "BOOKED");
        if (excludeAppointmentId != null) {
            collisions.removeIf(a -> a.getAppointmentId().equals(excludeAppointmentId));
        }
        if (!collisions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Time slot not available for the selected doctor.");
        }
    }

    // SEND NOTIFICATION (calls Notification Service)
    private void sendNotification(Appointment appointment, String type, String message) {
        try {
            Map<String, Object> notification = Map.of(
                    "type", "EMAIL",
                    "referenceId", appointment.getAppointmentId(),
                    "recipient", "ishaan5shaan@gmail.com", // can later fetch patient email
                    "message", message,
                    "status", appointment.getStatus()
            );
            restTemplate.postForEntity(notificationServiceUrl + "/send", notification, Void.class);
        } catch (Exception e) {
            System.err.println("Failed to send notification for appointment " + appointment.getAppointmentId() + e.getMessage());
            e.printStackTrace();
        }
    }
}
