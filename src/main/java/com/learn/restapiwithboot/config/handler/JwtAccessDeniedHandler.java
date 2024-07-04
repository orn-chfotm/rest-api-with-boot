package com.learn.restapiwithboot.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.core.dto.response.FailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Unauthoriz Token Error : {}", accessDeniedException.getMessage());
        setResponse(response);
        response.getWriter().write(objectMapper.writeValueAsString(setException(accessDeniedException)));
    }

    private FailResponse setException(AccessDeniedException authException) {
        return new FailResponse(SC_UNAUTHORIZED, authException.getMessage());
    }

    private void setResponse(HttpServletResponse response) {
        response.setStatus(SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
