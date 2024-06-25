package com.learn.restapiwithboot.reservation.controller;

import com.learn.restapiwithboot.meeting.common.BaseTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends BaseTest {

    @Test
    void getReservations() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(get("/api/reservation"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}