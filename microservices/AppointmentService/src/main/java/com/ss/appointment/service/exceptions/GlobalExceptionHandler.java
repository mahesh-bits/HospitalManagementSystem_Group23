package com.ss.appointment.service.exceptions;

import com.ss.appointment.service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .code(ex.getStatusCode().value())
                .message(ex.getReason())
                .correlationId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse error = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error: " + ex.getMessage())
                .correlationId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
