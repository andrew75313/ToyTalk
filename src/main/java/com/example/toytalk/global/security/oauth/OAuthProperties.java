package com.example.toytalk.global.security.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "oauth")
@Getter
@Setter
public class OAuthProperties {

    private Map<String, Provider> providers;

    @Getter
    @Setter
    public static class Provider {
        private String clientId;
        private String redirectUri;
        private String tokenUri;
        private String userinfoUri;
    }
}