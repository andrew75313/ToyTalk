package com.example.toytalk.domain.chat.dto;

import java.time.Instant;

public record ChatResponseDTO(String username, String content, Instant sentAt) {
}
