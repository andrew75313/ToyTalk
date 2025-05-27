package com.example.toytalk.global.security.auth.service;

import com.example.toytalk.domain.users.entity.User;
import com.example.toytalk.domain.users.repository.UserRepository;
import com.example.toytalk.global.redis.service.RedisService;
import com.example.toytalk.global.security.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Transactional
    public String refreshAccessToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid Refresh Token. Login again.");
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        String redisRefreshToken = redisService.getRefreshToken(userId);

        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("Refresh token not found in Redis or does not match.");
        }

        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(
                () -> new UsernameNotFoundException("No user found.")
        );

        return jwtUtil.createAccessToken(user);
    }

    @Transactional
    public String refreshRefreshToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid Refresh Token. Login again.");
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(
                () -> new UsernameNotFoundException("No user found.")
        );

        String newRefreshToken = jwtUtil.createRefreshToken(user);

        redisService.saveRefreshToken(userId, newRefreshToken);

        return newRefreshToken;
    }
}
