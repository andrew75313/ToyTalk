package com.example.toytalk.domain.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthUserInfoDTO {
    private Long id;
    private String email;

    public OAuthUserInfoDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
