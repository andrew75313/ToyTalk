package com.example.toytalk.global.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDTO {
    private String error;
    private String errorMessage;
    private LocalDateTime timestamp;
}
