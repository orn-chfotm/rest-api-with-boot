package com.learn.restapiwithboot.auth.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.auth.dto.response.AuthResponse;
import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import com.learn.restapiwithboot.core.exceptions.enums.ExceptionType;
import com.learn.restapiwithboot.core.exceptions.impl.ResourceNotFoundException;
import com.learn.restapiwithboot.core.exceptions.impl.TokenInvalidException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtProperties jwtProperties;

    public AuthResponse getRefresh(String refreshToken) {
        if (!this.jwtTokenProvider.validateToken(refreshToken, this.jwtProperties.getRefreshToken().getSecretKey())) {
            log.warn("Refresh Token이 유효하지 않습니다.");
            throw ExceptionType.INVALID_REFRESH_TOKEN_EXCEPTION.getException();
        }

        Claims claims = this.jwtTokenProvider.getClaims(refreshToken, this.jwtProperties.getRefreshToken().getSecretKey());
        String email = claims.get("email").toString();

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(ExceptionType.ACCOUNT_NOT_FOUND::getException);

        return AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(this.jwtTokenProvider.generateAccessToken(account))
                .refreshToken(this.jwtTokenProvider.generateRefreshToken(account))
                .build();
    }
}
