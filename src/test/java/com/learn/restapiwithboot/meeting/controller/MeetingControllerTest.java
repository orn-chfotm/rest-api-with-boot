package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.meeting.common.BaseTest;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingControllerTest extends BaseTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    @DisplayName("전체 회의 목록을 조회한다. - 성공 시")
    void testGetAllMeeting() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(get("/api/meeting"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("특정 회의를 조회한다. - 성공 시")
    void testGetMeeting() throws Exception {
        meetingRepository.save(createMeeting());

        // Given
        Long id = 1L;

        // When && Then
        mockMvc.perform(get("/api/meeting/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회의를 등록한다. - 성공 시")
    void testCreateMeeting() throws Exception {
        // Given
        Meeting meeting = createMeeting();
        // When && Then
        mockMvc.perform(post("/api/meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(meeting))
        )
            .andDo(print())
            .andExpect(status().isOk())
        ;
    }

    private Meeting createMeeting() {
        Meeting meeting = Meeting.builder()
                .title("회의 제목")
                .content("회의 내용")
                .description("회의 설명")
                .meetingType(MeetingType.ONLINE)
                .place(Place.builder()
                        .name("장소 이름")
                        .palceType(PlaceType.CAFE)
                        .build())
                .address(null)
                .build();
        return meeting;
    }
}