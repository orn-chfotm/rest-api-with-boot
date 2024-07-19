package com.learn.restapiwithboot.account.controller;

import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends BaseTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AccountRequest createAccount() {
        return AccountRequest.builder()
                .email("userTest@email.com")
                .password("1234")
                .roles(Set.of(AccountRole.USER))
                .gender("man")
                .phoneNumber("010-1234-5678")
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void putAccount() throws Exception {
        // Given
        AccountRequest request = createAccount();

        // When & Then
        mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.email", is(request.getEmail())))
                .andExpect(jsonPath("data.roles", hasSize(1)))
                .andExpect(jsonPath("data.roles", contains("USER")))
                .andDo(document("create-account",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type")
                        ),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("roles").description("권한"),
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
                                fieldWithPath("data.roles[]").description("권한"),
                                fieldWithPath("data.gender").description("성별"),
                                fieldWithPath("data.phoneNumber").description("전화번호")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("PW 암호화 테스트")
    void encodePassword() {
        // Given
        String password = "1234";

        // When
        String encodedPassword = passwordEncoder.encode(password);

        // Then
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }


}