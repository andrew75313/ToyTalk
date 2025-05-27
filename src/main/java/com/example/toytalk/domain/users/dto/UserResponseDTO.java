package com.example.toytalk.domain.users.dto;

import com.example.toytalk.domain.users.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private String oauthProvider;
    private String oauthId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.oauthProvider = user.getOauthProvider();
        this.oauthId = user.getOauthId();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
