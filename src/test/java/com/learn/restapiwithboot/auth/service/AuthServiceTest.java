package com.learn.restapiwithboot.auth.service;

import com.learn.restapiwithboot.auth.dto.request.AuthRequest;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServiceTest extends BaseTest {

    @Test
    @DisplayName("Token 발급 테스트 - 성공")
    void getAuth() throws Exception {
        // given
        AuthRequest reqeust = AuthRequest.builder()
                .email("user@email.com")
                .password("1234")
                .build();

        // when && then
        mockMvc.perform(post("/api/auth")
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
        mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reqeust))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}