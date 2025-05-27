package com.example.toytalk.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataResponseDTO<T> {
    private T data;

    @Builder
    public DataResponseDTO(T data) {
        this.data = data;
    }
}
