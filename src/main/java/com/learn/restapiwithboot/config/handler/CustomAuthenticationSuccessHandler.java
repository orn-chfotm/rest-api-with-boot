package com.learn.restapiwithboot.config.handler;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.auth.dto.response.AuthResponse;
import com.learn.restapiwithboot.config.authentication.CustomUser;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.core.util.HttpServletUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletUtils httpServletUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Account account = ((CustomUser) authentication.getPrincipal()).getAccount();

        AuthResponse authResponse = AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(this.jwtTokenProvider.generateAccessToken(account))
                .refreshToken(this.jwtTokenProvider.generateRefreshToken(account))
                .build();

        httpServletUtils.setHandlerResponse(HttpStatus.OK, authResponse);
    }
}