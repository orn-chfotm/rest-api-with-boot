package com.learn.restapiwithboot.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.auth.dto.response.AuthResponse;
import com.learn.restapiwithboot.config.authentication.CustomUser;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.core.handler.response.HandlerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final HandlerResponse handlerResponse;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Account account = ((CustomUser) authentication.getPrincipal()).getAccount();

        AuthResponse authResponse = AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(this.jwtTokenProvider.generateAccessToken(account))
                .refreshToken(this.jwtTokenProvider.generateRefreshToken(account))
                .build();

        handlerResponse.setHandlerResponse(response, SC_OK, authResponse);
    }
}