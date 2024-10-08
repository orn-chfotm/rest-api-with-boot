package com.learn.restapiwithboot.auth.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.config.properties.JwtProperties;
import com.learn.restapiwithboot.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void getLoginTest() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .email("user@email.com")
                .password("1234")
                .build();

        // when && then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Token 발급 테스트 - Login 성공")
    void getAuthTest() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .email("user@email.com")
                .password("1234")
                .build();

        // when && then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(request))
                        .param("email", request.getEmail())
                        .param("password", request.getPassword())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-access-token",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        requestFields(
                                fieldWithPath("email").description("Login email"),
                                fieldWithPath("password").description("Login password")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        responseFields(
                                fieldWithPath("email").description("Access eamil"),
                                fieldWithPath("accessToken").description("Access Token"),
                                fieldWithPath("refreshToken").description("Refresh Token")
                        )
                    )
                );
    }

    @Test
    @DisplayName("Token 발급 테스트 - 실패")
    void getAuthFailTest() throws Exception {
        // given
        AuthRequest request = AuthRequest.builder()
                .email("fail@email.com")
                .password("1234")
                .build();

        // when && then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Refresh토큰을 이용한 Token 재 발급 테스트 - 성공")
    void getAuthUseRefreshTokenTest() throws Exception {
        // given
        Account account = accountRepository.findByEmail("user@email.com").get();

        String refreshToken = this.jwtTokenProvider.generateRefreshToken(account);

        // when && then
        ResultActions resultActions = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("refreshToken", refreshToken)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.email", is(account.getEmail())))
                .andExpect(jsonPath("data.accessToken", is(notNullValue())))
                .andExpect(jsonPath("data.refreshToken", is(notNullValue())))
                .andDo(document("get-refresh-token",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        requestParameters(
                                parameterWithName("refreshToken").description("Refresh Token")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("Status Code"),
                                fieldWithPath("message").description("Message"),
                                fieldWithPath("data.email").description("Access eamil"),
                                fieldWithPath("data.accessToken").description("Access Token"),
                                fieldWithPath("data.refreshToken").description("Refresh Token")
                        )
                ));
    }

    @Test
    @DisplayName("Refresh토큰을 이용한 Token 재 발급 테스트 - 실패")
    void getAuthUseRefreshTokenFailTest() throws Exception {
        // given
        Account account = accountRepository.findByEmail("user@email.com").get();

        String refreshToken = this.jwtTokenProvider.generateRefreshToken(account);

        // when && then
        ResultActions resultActions = mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("refreshToken", refreshToken + "fail") // 잘못된 RefreshToken
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}