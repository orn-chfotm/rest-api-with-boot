package com.learn.restapiwithboot.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.core.dto.response.FailResponse;
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

@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    private final int STATUS_CODE = HttpServletResponse.SC_UNAUTHORIZED;

    public JwtEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthoriz Token Error : {}", authException.getMessage());
        setResponse(response);
        response.getWriter().write(objectMapper.writeValueAsString(setException(authException)));
    }

    private FailResponse setException(AuthenticationException authException) {
        return new FailResponse(STATUS_CODE, authException.getMessage());
    }

    private void setResponse(HttpServletResponse response) {
        response.setStatus(STATUS_CODE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
