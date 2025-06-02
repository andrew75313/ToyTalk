package com.example.toytalk.global.security.handler;

import com.example.toytalk.domain.users.entity.User;
import com.example.toytalk.global.security.user.UserDetailsImpl;
import com.example.toytalk.global.security.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        String accessToken = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user);

        redisTemplate.opsForValue().set(
                "RefreshToken: " + user.getId(),
                refreshToken,
                Duration.ofMillis(jwtUtil.getRefreshTokenTime())
        );

        Cookie accessTokenCookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, accessToken.substring(7));
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie(JwtUtil.REFRESHTOKEN_HEADER, refreshToken.substring(7));
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        response.sendRedirect("/");
    }
}
