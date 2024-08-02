package com.learn.restapiwithboot.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.handler.response.HandlerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerResponse handlerResponse;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("AuthenticationEntryPoint :: {}", authException.getMessage());
        FailResponse<Object> failResponse = new FailResponse<>(SC_FORBIDDEN, authException.getMessage());

        handlerResponse.setHandlerResponse(response, SC_FORBIDDEN, failResponse);
    }
}