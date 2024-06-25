package com.learn.restapiwithboot.account.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountControllerTest extends BaseTest {

    @Autowired
    ObjectMapper objectMapper;

    private Account createAccount() {
        return Account.builder()
                .email("user@email.com")
                .password("1234")
                .build();
    }

    @Test
    void putAccount() throws Exception {
        // Given
        Account account = createAccount();

        // When
        mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(account))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.email", is(account.getEmail())))
                .andExpect(jsonPath("data.password", is(account.getPassword())));
    }


}