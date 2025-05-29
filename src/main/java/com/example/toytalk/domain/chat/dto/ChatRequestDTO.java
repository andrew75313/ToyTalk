package com.example.toytalk.domain.chat.dto;

import java.time.Instant;

public record ChatRequestDTO(String userId, String content, Instant sentAt) {
}

