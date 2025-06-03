package com.example.toytalk.domain.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OAuthUserInfoDTO {
    private String id;
    private String email;

    public OAuthUserInfoDTO(String id, String email) {
        this.id = id;
        this.email = email;
    }
}
