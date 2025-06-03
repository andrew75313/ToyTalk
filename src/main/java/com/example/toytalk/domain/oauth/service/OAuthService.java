package com.example.toytalk.domain.oauth.service;

import com.example.toytalk.domain.oauth.dto.LoginResponse;
import com.example.toytalk.domain.oauth.dto.OAuthUserInfoDTO;
import com.example.toytalk.domain.users.entity.User;
import com.example.toytalk.domain.users.entity.UserRole;
import com.example.toytalk.domain.users.entity.UserStatus;
import com.example.toytalk.domain.users.repository.UserRepository;
import com.example.toytalk.global.security.oauth.OAuthProperties;
import com.example.toytalk.global.security.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final OAuthProperties oauthProperties;
    private final JwtUtil jwtUtil;
    private final RedisTemplate redisTemplate;
    private final RestTemplate restTemplate;

    public OAuthUserInfoDTO getKakaoUser(String code) throws JsonProcessingException {

        String kakaoAccessToken = getToken(code);

        return  getKakaoUserInfo(kakaoAccessToken);
    }


    public LoginResponse kakaoLogin(OAuthUserInfoDTO userInfo) {
        User kakaoUser = userRepository.findByOauthId(userInfo.getId()).orElseGet(
                () -> registerOAuthUser("kakao", userInfo)
        );

        String accessToken = jwtUtil.createAccessToken(kakaoUser);
        String refreshToken = jwtUtil.createRefreshToken(kakaoUser);

        redisTemplate.opsForValue().set(
                "RefreshToken: " + kakaoUser.getId(),
                refreshToken,
                Duration.ofMillis(jwtUtil.getRefreshTokenTime())
        );

        return new LoginResponse(kakaoUser, accessToken, refreshToken);
    }

    private User registerOAuthUser(String provider, OAuthUserInfoDTO infoDto) {
        String oauthId = infoDto.getId().toString();
        String email = infoDto.getEmail();

        User user = User.builder()
                .oauthProvider(provider)
                .oauthId(oauthId)
                .email(email)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVATED)
                .build();

        userRepository.save(user);

        return user;
    }

    private OAuthUserInfoDTO getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString(oauthProperties.getProviders().get("kakao").getUserinfoUri())
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        String id = jsonNode.get("id").toString();
//        String email = jsonNode.get("kakao_account")
//                .get("email").asText();
        String email = null;

        return new OAuthUserInfoDTO(id, email);
    }

    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString(oauthProperties.getProviders().get("kakao").getTokenUri())
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", oauthProperties.getProviders().get("kakao").getClientId());
        body.add("redirect_uri", oauthProperties.getProviders().get("kakao").getRedirectUri());
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }
}
