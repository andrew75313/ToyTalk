package com.example.toytalk.domain.oauth.controller;

import com.example.toytalk.domain.oauth.dto.LoginResponse;
import com.example.toytalk.domain.oauth.service.OAuthService;
import com.example.toytalk.domain.users.dto.UserResponseDTO;
import com.example.toytalk.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OAuthService oauthService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<UserResponseDTO> kakaoLogin(@RequestParam String code) throws IOException {
        LoginResponse loginResponse = oauthService.kakaoLogin(code);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtUtil.AUTHORIZATION_HEADER, loginResponse.getAccessToken());
        headers.add(JwtUtil.REFRESHTOKEN_HEADER, loginResponse.getRefreshToken());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UserResponseDTO(loginResponse.getUser()));
    }
}
