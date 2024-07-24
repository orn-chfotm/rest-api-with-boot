package com.learn.restapiwithboot.reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.domain.enums.Gender;
import com.learn.restapiwithboot.account.dto.request.AccountRequest;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.config.provider.JwtTokenProvider;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends BaseTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders getHeader(Account account) {
        String token = getToken(account);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
        return httpHeaders;
    }

    private String getToken(Account account) {
        return jwtTokenProvider.generateAccessToken(account);
    }

    @Test
    @DisplayName("계정 Email 기준으로 예약을 조회한다.")
    void getReservations() throws Exception {
        // Given
        Meeting meeting = createMeeting();

        Account account = accountRepository.findByEmail("user@email.com")
                .orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));

        HttpHeaders header = getHeader(account);

        reservationRepository.save(Reservation.builder()
                .accountId(account.getId())
                .meetingId(meeting.getId())
                .build()
        );

        mockMvc.perform(get("/api/reservation")
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(header)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createDate,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약을 생성한다. - 성공")
    void createReservation() throws Exception {
        // Given
        Account account = createAccount();
        Meeting meeting = createMeeting();

        ReservationRequest reservation = ReservationRequest.builder()
                .meetingId(meeting.getId())
                .build();

        // When
        mockMvc.perform(post("/api/reservation")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(reservation))
                    .headers(getHeader(account))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").exists())
        ;
    }

    @Test
    @DisplayName("예약을 생성한다. - 실패")
    void createReservationFail() throws Exception {
        // Given
        Optional<Account> getAccount = accountRepository.findByEmail("user@email.com");
        Reservation request = Reservation.builder()
                .accountId(1L)
                .meetingId(1L)
                .build();

        // When && Then
        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .headers(getHeader(getAccount.get()))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("예약을 취소한다.")
    void deleteReservation() throws Exception {
        // Given
        Account account = this.createAccount();
        Meeting meeting = this.createMeeting();

        ReservationRequest request = ReservationRequest.builder()
                .meetingId(meeting.getId())
                .build();

        ResultActions resultActions = mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .headers(getHeader(account))
                )
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);
        String reservationId = jsonNode.path("data").path("id").toString();

        // When
        ResultActions perform = mockMvc.perform(
                delete("/api/reservation/{id}", reservationId)
                        .headers(getHeader(account))
        );

        // Then
        perform
                .andDo(print())
                .andExpect(status().isOk())
        ;

    }

    @Test
    @DisplayName("계정 Email를 이용하여 계정 Id를 조회한다.")
    void getAccountTest() {
        // Given
        Account account = createAccount();
        // When
        Account getAccount = accountRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 계정이 없습니다."));
        // Then
        assertThat(getAccount.getId()).isNotNull();
    }

    private Account createAccount() {
        Account account = Account.builder()
                .email("usertest@email.com")
                .password("1234")
                .phoneNumber("010-1234-5678")
                .gender(Gender.getName("M"))
                .roles(Collections.singleton(AccountRole.USER))
                .build();

        return accountRepository.save(account);
    }

    private Meeting createMeeting() {
        Optional<Account> getAcccount = accountRepository.findByEmail("user@email.com");

        Meeting meeting = Meeting.builder()
                .title("회의 제목")
                .content("회의 내용")
                .description("회의 설명")
                .meetingType(MeetingType.ONLINE)
                .place(Place.builder()
                        .name("장소 이름")
                        .placeType(PlaceType.CAFE)
                        .address(Address.builder().build())
                        .build())
                .dues(10000)
                .regId(getAcccount.get().getId())
                .build();

        return meetingRepository.save(meeting);
    }

}