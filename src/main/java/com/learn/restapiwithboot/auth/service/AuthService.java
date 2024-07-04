package com.learn.restapiwithboot.auth.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.auth.dto.response.AuthResponse;
import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.config.provider.properties.JwtProperties;
import com.learn.restapiwithboot.core.exceptions.ResourceNotFoundException;
import com.learn.restapiwithboot.core.exceptions.TokenInvalidException;
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

    public AuthResponse getAuth(AuthRequest authRequest) {
        Account account = accountRepository.findByEmail(authRequest.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("해당 이메일을 가진 계정이 존재하지 않습니다.")
        );

        if (!passwordEncoder.matches(authRequest.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(this.jwtTokenProvider.generateAsseccToken(account))
                .refreshToken(this.jwtTokenProvider.generateRefreshToken(account))
                .build();
    }

    public AuthResponse getRefresh(String refreshToken) {
        if (!this.jwtTokenProvider.validateToken(refreshToken, this.jwtProperties.getRefreshSecretKey())) {
            log.warn("Refresh Token이 유효하지 않습니다.");
            throw new TokenInvalidException("Refresh Token이 유효하지 않습니다.");
        }

        Claims claims = this.jwtTokenProvider.getClaims(refreshToken, this.jwtProperties.getRefreshSecretKey());
        String email = claims.get("email").toString();

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 사용자가 없습니다."));

        return AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(this.jwtTokenProvider.generateAsseccToken(account))
                .refreshToken(this.jwtTokenProvider.generateRefreshToken(account))
                .build();
    }
}