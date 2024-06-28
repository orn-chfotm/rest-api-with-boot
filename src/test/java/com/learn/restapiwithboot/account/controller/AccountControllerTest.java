package com.learn.restapiwithboot.account.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends BaseTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Account createAccount() {
        return Account.builder()
                .email("userTest@email.com")
                .password("1234")
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void putAccount() throws Exception {
        // Given
        Account account = createAccount();

        // When & Then
        mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(account))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.email", is(account.getEmail())))
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    JsonNode jsonNode = objectMapper.readTree(contentAsString).path("data");
                    assertTrue(
                            passwordEncoder.matches(account.getPassword(), jsonNode.get("password").asText())
                    );
                })
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