package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountControllerTest extends BaseTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders getHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
        return httpHeaders;
    }

    private AccountRequest createAccount(String email) {
        return AccountRequest.builder()
                .email(email)
                .password("1234")
                .gender("M")
                .phoneNumber("010-1234-5678")
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void putAccountTest() throws Exception {
        // Given
        AccountRequest request = createAccount("userTest1@eamil.com");

        // When & Then
        mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.email", is(request.getEmail())))
                .andExpect(jsonPath("data.role", is("USER")))
                .andDo(document("create-account",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("phoneNumber").description("전화번호")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("statusCode").description("상태코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.role").description("권한"),
                                fieldWithPath("data.gender").description("성별"),
                                fieldWithPath("data.phoneNumber").description("전화번호")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    void getAccountInfoTest() throws Exception {
        // Given
        Optional<Account> getAccount = accountRepository.findByEmail("user@email.com");
        String accessToken = jwtTokenProvider.generateAccessToken(getAccount.get());

        // When & Then
        mockMvc.perform(get("/api/account")
                        .headers(getHeader(accessToken)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-account-info",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer Token")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("statusCode").description("상태코드"),
                                fieldWithPath("message").description("상태 메시지"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.role").description("권한"),
                                fieldWithPath("data.gender").description("성별"),
                                fieldWithPath("data.phoneNumber").description("전화번호")
                        )
                ));
    }

   @Test
   @DisplayName("회원 탈퇴 테스트")
    void deleteAccountTest() throws Exception {
       // Given
       String email = "userTest2@eamil.com";
       AccountRequest request = createAccount(email);
       mockMvc.perform(post("/api/account")
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .content(objectMapper.writeValueAsString(request))
       );

       Optional<Account> getAccount = accountRepository.findByEmail(email);
       String accessToken = jwtTokenProvider.generateAccessToken(getAccount.get());

        // When & Then
       mockMvc.perform(delete("/api/account")
                       .headers(getHeader(accessToken))
               )
               .andDo(print())
               .andExpect(status().isOk())
               .andDo(document("delete-account",
                       requestHeaders(
                               headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer Token")
                       ),
                       responseHeaders(
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                       ),
                       relaxedResponseFields(
                               fieldWithPath("statusCode").description("상태코드"),
                               fieldWithPath("message").description("상태 메시지")
                       )
               ));

   }
}