package com.learn.restapiwithboot.common.util;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class JwtTokenProviderTest extends BaseTest {

    @Autowired
    private JwtTokenProvider jwtProvider;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("토큰 생성 테스트")
    void generateToken() {
        // given
        String email = "user@email.com";

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        String accessToken = jwtProvider.generateAccessToken(account);
        String refreshToken = jwtProvider.generateRefreshToken(account);

        assertThat(accessToken).isNotEmpty();
        assertThat(refreshToken).isNotEmpty();
    }

    @Test
    @DisplayName("토큰 검증 테스트")
    void getAuthentication() {
        // given
        String email = "user@email.com";

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        String accessToken = jwtProvider.generateAccessToken(account);
        String refreshToken = jwtProvider.generateRefreshToken(account);

        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        Authentication authentication1 = jwtProvider.getAuthentication(refreshToken);

        System.out.println(StandardCharsets.UTF_8.toString());

        assertThat(authentication).isNotNull();
        assertWith(authentication1).isNotNull();
    }

}