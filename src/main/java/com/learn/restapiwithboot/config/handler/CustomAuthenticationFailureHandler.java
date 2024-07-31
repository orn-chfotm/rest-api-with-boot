package com.learn.restapiwithboot.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.core.dto.response.FailResponse;
import com.learn.restapiwithboot.core.handler.response.HandlerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final HandlerResponse authenticationResponseHandler;

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("Login Fail : {}", exception.getMessage());
        FailResponse<Object> failResponse = new FailResponse<>(SC_UNAUTHORIZED, exception.getMessage());

        authenticationResponseHandler.setHandlerResponse(response, SC_UNAUTHORIZED, failResponse);
    }
}
