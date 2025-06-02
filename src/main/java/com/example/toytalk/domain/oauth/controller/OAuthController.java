package com.example.toytalk.domain.oauth.controller;

import com.example.toytalk.domain.oauth.dto.LoginResponse;
import com.example.toytalk.domain.oauth.service.OAuthService;
import com.example.toytalk.global.security.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String kakaoLogin(@RequestParam String code,
                             HttpServletResponse response,
                             Model model) throws IOException {
        LoginResponse loginResponse;

        try {
            loginResponse = oauthService.kakaoLogin(code);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }

        Cookie accessTokenCookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, loginResponse.getAccessToken().substring(7));
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie(JwtUtil.REFRESHTOKEN_HEADER, loginResponse.getRefreshToken().substring(7));
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        return "redirect:/";
    }
}
