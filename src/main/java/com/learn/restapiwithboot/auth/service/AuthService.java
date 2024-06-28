package com.learn.restapiwithboot.auth.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.auth.dto.response.AuthResponse;
import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import com.learn.restapiwithboot.common.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthService(AccountRepository accountRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse getAuth(AuthRequest authRequest) {
        Account account = accountRepository.findByEmail(authRequest.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("해당 이메일을 가진 계정이 존재하지 않습니다.")
        );

        if (!passwordEncoder.matches(authRequest.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return AuthResponse.builder()
                .email(account.getEmail())
                .accessToken(jwtUtil.generateAccessToken(account))
                .refreshToken(jwtUtil.generateRefreshToken(account))
                .build();
    }
}
