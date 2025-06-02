package com.example.toytalk.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String msg;

        if (exception instanceof UsernameNotFoundException) {
            msg = "해당 사용자를 찾을 수 없습니다.";
        } else if (exception instanceof BadCredentialsException) {
            msg = "아이디 또는 비밀번호를 다시 확인해주세요.";
        } else {
            msg = "로그인에 실패했습니다.";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String json = new ObjectMapper().writeValueAsString(
                Map.of("statusCode", 401, "msg", msg)
        );

        response.getWriter().write(json);
    }
}
