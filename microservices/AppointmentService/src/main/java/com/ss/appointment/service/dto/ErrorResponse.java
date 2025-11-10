package com.ss.appointment.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String message;
    private String correlationId;
    private Instant timestamp;
    private String path;
}
