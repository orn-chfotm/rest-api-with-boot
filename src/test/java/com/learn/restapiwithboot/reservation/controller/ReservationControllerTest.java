package com.learn.restapiwithboot.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.meeting.common.BaseTest;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends BaseTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        accountRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void getReservations() throws Exception {
        // Given
        Account account = createAccount();
        Meeting meeting = createMeeting();

        reservationRepository.save(Reservation.builder()
                .accountId(account.getId())
                .meetingId(meeting.getId())
                .build()
        );

        mockMvc.perform(get("/api/reservation")
                        .param("email", account.getEmail()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("계정 Email를 이용하여 계정 Id를 조회한다.")
    void getAccountTest() {
        // Given
        Account account = createAccount();
        accountRepository.save(account);
        // When
        Long accountId = accountRepository.findIdByEmail(account.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 계정이 없습니다."));
        // Then
        assertThat(accountId).isNotNull();
    }

    private Account createAccount() {
        Account account = Account.builder()
                .email("user@email.com")
                .password("1234")
                .build();

        return accountRepository.save(account);
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
                        .address(Address.builder().build())
                        .build())
                .build();

        return meetingRepository.save(meeting);
    }

}