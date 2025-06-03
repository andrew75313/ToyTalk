package com.example.toytalk.domain.oauth.controller;

import com.example.toytalk.domain.oauth.dto.LoginResponse;
import com.example.toytalk.domain.oauth.dto.OAuthUserInfoDTO;
import com.example.toytalk.domain.oauth.service.OAuthService;
import com.example.toytalk.global.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {

    private final OAuthService oauthService;


    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam String code,
                                Model model) throws IOException {
        OAuthUserInfoDTO userInfo = oauthService.getKakaoUser(code);
        model.addAttribute("userInfo", userInfo);

        return "kakao-login";
    }

    @PostMapping("/kakao/login")
    public void kakaoLogin(@RequestBody OAuthUserInfoDTO userInfo,
                           HttpServletResponse response) throws IOException {
        try {
            LoginResponse loginResponse = oauthService.kakaoLogin(userInfo);

            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, loginResponse.getAccessToken());
            response.addHeader(JwtUtil.REFRESHTOKEN_HEADER, loginResponse.getRefreshToken());

            response.setContentType("application/json;charset=UTF-8");
            String json = new ObjectMapper().writeValueAsString(
                    Map.of("statusCode", 200, "msg", "로그인 성공")
            );

            response.getWriter().write(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");

            String json = new ObjectMapper().writeValueAsString(
                    Map.of("statusCode", 401, "msg", e.getMessage())
            );

            response.getWriter().write(json);
        }
    }
}
