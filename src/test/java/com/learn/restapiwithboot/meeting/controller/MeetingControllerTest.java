package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingControllerTest extends BaseTest {

    @Test
    @DisplayName("전체 회의 목록을 조회한다. - 성공 시")
    void testGetAllMeeting() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(get("/api/meeting"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}