package com.example.toytalk.global.websocket.interceptor;

import com.example.toytalk.domain.chatroom.repository.ChatroomMemberRepository;
import com.example.toytalk.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/subscribe/chat/")) {
            List<String> authHeaders = accessor.getNativeHeader(JwtUtil.AUTHORIZATION_HEADER);
            if (authHeaders == null || authHeaders.isEmpty()) {
                throw new AccessDeniedException("Authorization header is missing");
            }

            String token = authHeaders.get(0);
            if (StringUtils.hasText(token) && token.startsWith(JwtUtil.BEARER_PREFIX)) {
                token = token.substring(7);
            }

            if(!jwtUtil.validateToken(token)) {
                throw new AccessDeniedException("권한이 없습니다.");
            }
        }

        return message;
    }
}
