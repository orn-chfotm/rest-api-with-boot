package com.learn.restapiwithboot.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.auth.dto.response.AuthResponse;
import com.learn.restapiwithboot.config.token.CustomUser;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login Success Process Start");
        Account account = ((CustomUser) authentication.getPrincipal()).getAccount();

        setResponse(response);
        response.getWriter().write(objectMapper.writeValueAsString(getSucceeResponse(account)));

        log.info("Login Success Process End");
    }

    private AuthResponse getSucceeResponse (Account account) {
        log.info("Login Success Useremail {}", account.getEmail());
        log.info("generate Token");
        return AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(this.jwtTokenProvider.accessToken(account))
                .refreshToken(this.jwtTokenProvider.refreshToken(account))
                .build();
    }

    private void setResponse(HttpServletResponse response) {
        response.setStatus(SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}