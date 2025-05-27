package com.example.toytalk.global.redis.service;

import com.example.toytalk.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    public String getRefreshToken(String userId) {

        String key = "RefreshToken: " + userId;
        return redisTemplate.opsForValue().get(key).substring(7);
    }


    public void saveRefreshToken(String userId, String refreshToken) {

        redisTemplate.opsForValue().set(
                "RefreshToken: " + userId,
                refreshToken,
                Duration.ofMillis(jwtUtil.getRefreshTokenTime())
        );
    }
}
