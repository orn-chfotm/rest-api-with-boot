package com.learn.restapiwithboot.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.config.provider.properties.JwtProperties;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Token 발급 테스트 - 성공")
    void getAuth() throws Exception {
        // given
        AuthRequest reqeust = AuthRequest.builder()
                .email("user@email.com")
                .password("1234")
                .build();

        // when && then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reqeust))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Token 발급 테스트 - 실패")
    void getAuthFail() throws Exception {
        // given
        AuthRequest reqeust = AuthRequest.builder()
                .email("fail@email.com")
                .password("1234")
                .build();

        // when && then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reqeust))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Refresh토큰을 이용한 Token 재 발급 테스트 - 성공")
    void getAuthUseRefreshToken() throws Exception {
        // given
        Account account = accountRepository.findByEmail("user@email.com").get();

        String refreshToken = this.jwtTokenProvider.generateToken(
                account,
                this.jwtProperties.getRefreshSecretKey(),
                this.jwtProperties.getRefreshExpTime()
        );

        // when && then
        ResultActions resultActions = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("refreshToken", refreshToken)
                )
                .andDo(print())
                .andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(resultActions.andReturn().getResponse().getContentAsString());
        System.out.println(jsonNode.toString());
        assertThat(jsonNode.get("data").get("accessToken").toString()).isNotEmpty();
    }

    @Test
    @DisplayName("Refresh토큰을 이용한 Token 재 발급 테스트 - 실패")
    void getAuthUseRefreshTokenFail() throws Exception {
        // given
        Account account = accountRepository.findByEmail("user@email.com").get();

        String refreshToken = this.jwtTokenProvider.generateToken(
                account,
                this.jwtProperties.getRefreshSecretKey(),
                this.jwtProperties.getRefreshExpTime()
        );

        // when && then
        ResultActions resultActions = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("refreshToken", refreshToken + "fail") // 잘못된 RefreshToken
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}