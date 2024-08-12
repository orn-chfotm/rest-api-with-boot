package com.learn.restapiwithboot.auth.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.auth.dto.response.AuthResponse;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;
import com.learn.restapiwithboot.core.exceptions.enums.impl.AccountErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.CredentialsErrorType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse getRefresh(String refreshToken) {
        if (!this.jwtTokenProvider.refreshTokenValidate(refreshToken)) {
            log.warn("Refresh Token이 유효하지 않습니다.");
            throw new BaseException(CredentialsErrorType.INVALID_REFRESH_TOKEN);
        }

        Claims claims = this.jwtTokenProvider.getRefreshTokenClaims(refreshToken);
        String email = claims.get("email").toString();

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(AccountErrorType.ACCOUNT_NOT_FOUND));

        return AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(this.jwtTokenProvider.generateAccessToken(account))
                .refreshToken(this.jwtTokenProvider.generateRefreshToken(account))
                .build();
    }
}
