package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import com.learn.restapiwithboot.meeting.dto.request.AddressRequest;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.request.PlaceRequest;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MeetingControllerTest extends BaseTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders httpHeaders;

    @BeforeAll
    @DisplayName("모든 테스트 실행 전에 한 번 실행된다. - header 설정")
    void beforeAll() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String token = getToken();
        this.httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
    }

    @Test
    @DisplayName("전체 회의 목록을 조회한다. - 성공 시")
    void testGetAllMeeting() throws Exception {
        // Given
        Meeting meeting = createMeeting();

        String token = getToken();

        mockMvc.perform(get("/api/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private String getToken() {
        Long idByEmail = accountRepository.findIdByEmail("user@email.com").orElseThrow(
                () -> new IllegalArgumentException("가입되지 않은 이메일입니다.")
        );
        Account account = accountRepository.findById(idByEmail)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        String accessToken = jwtTokenProvider.generateAsseccToken(account);

        return accessToken;
    }

    @Test
    @DisplayName("특정 회의를 조회한다. - 성공 시")
    void testGetMeeting() throws Exception {
        // Given
        Meeting meeting = createMeeting();
        Long id = meeting.getId();

        // When && Then
        mockMvc.perform(get("/api/meeting/{id}", id)
                        .headers(httpHeaders)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("특정 회의를 조회한다. - 실패 시")
    void testExceptionGetMeeting() throws Exception {
        // Given
        Long id = 3L;
        // When && Then
        mockMvc.perform(get("/api/meeting/{id}", id)
                        .headers(httpHeaders)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회의를 등록한다. - 성공 시")
    void testCreateMeeting() throws Exception {
        // Given
        MeetingRequest meeting = createSusseccReqeustMeeting();

        System.out.println(this.objectMapper.writeValueAsString(meeting));

        // When && Then
        mockMvc.perform(post("/api/meeting")
                .headers(httpHeaders)
                .characterEncoding("utf-8")
                .content(this.objectMapper.writeValueAsString(meeting))
        )
            .andDo(print())
            .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("회의를 등록한다. - 실패 시")
    void testFailCreateMeeting() throws Exception {
        // Given
        MeetingRequest meeting = createFailReqeustMeeting();

        // When && Then
        mockMvc.perform(post("/api/meeting")
                        .headers(httpHeaders)
                        .characterEncoding("utf-8")
                        .content(this.objectMapper.writeValueAsString(meeting))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("회의를 수정한다 - 성공 시")
    void testUpdateMeeting() throws Exception {
        // Given
        Meeting meeting = createMeeting();

        PlaceRequest place = PlaceRequest.builder()
                .name("수정된 장소 이름")
                .palceType("RESTAURANT")
                .address(AddressRequest.builder()
                        .city("서울시")
                        .roadName("서울로")
                        .postalCode("12345")
                        .state("서울")
                        .build())
                .build();
        MeetingRequest meetingRequest = MeetingRequest.builder()
                .title("수정된 회의 제목")
                .content("수정된 회의 내용")
                .description("수정된 회의 설명")
                .meetingType("OFFLINE")
                .place(place)
                .dues(0)
                .meetingDate(LocalDateTime.now().plusDays(1))
                .maxMember(12)
                .build();

        // When && Then
        mockMvc.perform(put("/api/meeting/{id}", meeting.getId())
                        .headers(httpHeaders)
                        .characterEncoding("utf-8")
                        .content(this.objectMapper.writeValueAsString(meetingRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    private MeetingRequest createSusseccReqeustMeeting() {
        PlaceRequest place = PlaceRequest.builder()
                .name("장소 이름")
                .palceType("CAFE")
                .address(AddressRequest.builder()
                        .city("서울시")
                        .roadName("서울로")
                        .postalCode("12345")
                        .state("서울")
                        .build())
                .build();
        return MeetingRequest.builder()
                .title("회의 제목")
                .content("회의 내용")
                .description("회의 설명")
                .meetingType("ONLINE")
                .place(place)
                .dues(100)
                .maxMember(10)
                .meetingDate(LocalDateTime.now().plusDays(1))
                .build();
    }

    private MeetingRequest createFailReqeustMeeting() {
        PlaceRequest place = PlaceRequest.builder()
                .name("장소 이름")
                .palceType("CAFE")
                .address(AddressRequest.builder()
                        .city("서울시")
                        .roadName("서울로")
                        .postalCode("12345")
                        .state("서울")
                        .build())
                .build();
        return MeetingRequest.builder()
                .title("회의 제목")
                .content("회의 내용")
                .description("회의 설명")
                .place(place)
                .build();
    }

    private Meeting createMeeting() {
        Place place = Place.builder()
                .name("장소 이름")
                .palceType(PlaceType.CAFE)
                .address(Address.builder().build())
                .build();
        Meeting meeting = Meeting.builder()
                .title("회의 제목")
                .content("회의 내용")
                .description("회의 설명")
                .meetingType(MeetingType.ONLINE)
                .place(place)
                .build();
        return meetingRepository.save(meeting);
    }
}