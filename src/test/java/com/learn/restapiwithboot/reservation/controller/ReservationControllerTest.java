package com.learn.restapiwithboot.reservation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.enums.AccountRole;
import com.learn.restapiwithboot.account.domain.enums.Gender;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.common.BaseTest;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.core.exceptions.enums.ExceptionType;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest extends BaseTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders getHeader(Account account) {
        String token = jwtTokenProvider.generateAccessToken(account);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
        return httpHeaders;
    }

    @BeforeEach
    void init() {
        reservationRepository.deleteAll();
    }

    @Test
    @DisplayName("Token 계정 기준으로 예약을 조회한다.")
    void getReservationsTest() throws Exception {
        // Given
        Meeting meeting = createMeeting(0);

        Account account = accountRepository.findByEmail("user@email.com")
                .orElseThrow(ExceptionType.ACCOUNT_NOT_FOUND::getException);

        reservationRepository.save(Reservation.builder()
                .account(account)
                .meeting(meeting)
                .build()
        );

        mockMvc.perform(get("/api/reservation")
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getHeader(account))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createDate,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-reservation-list",
                        requestHeaders(
                                headerWithName("content-type").description("Content Type")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        responseHeaders(
                                headerWithName("content-type").description("Content Type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("data.content[].id").description("Reservation ID"),
                                fieldWithPath("data.content[].accountResponse.email").description("Reservation person Eamil"),
                                fieldWithPath("data.content[].accountResponse.role").description("Reservatio sperson role"),
                                fieldWithPath("data.content[].accountResponse.gender").description("Reservation person gender"),
                                fieldWithPath("data.content[].accountResponse.phoneNumber").description("Reservation person phoneNumber"),
                                fieldWithPath("data.content[].meetingResponse.id").description("Reservation meet ID"),
                                fieldWithPath("data.content[].meetingResponse.title").description("Reservation meet title"),
                                fieldWithPath("data.content[].meetingResponse.content").description("Reservation meet content"),
                                fieldWithPath("data.content[].meetingResponse.description").description("Reservation meet description"),
                                fieldWithPath("data.content[].meetingResponse.meetingType").description("Reservation meet type"),
                                fieldWithPath("data.content[].meetingResponse.place").description("Reservation meet place"),
                                fieldWithPath("data.content[].meetingResponse.place.placeType").description("Reservation meet place Type"),
                                fieldWithPath("data.content[].meetingResponse.place.address").type(OBJECT).description("Reservation place address").optional(),
                                fieldWithPath("data.content[].meetingResponse.place.address.roadName").type(STRING).description("Reservation meet place road name").optional(),
                                fieldWithPath("data.content[].meetingResponse.place.address.city").type(STRING).description("Reservation meet place city name").optional(),
                                fieldWithPath("data.content[].meetingResponse.place.address.state").type(STRING).description("Reservation meet place state").optional(),
                                fieldWithPath("data.content[].meetingResponse.place.address.postalCode").type(STRING).description("Reservation meet place postal code").optional(),
                                fieldWithPath("data.content[].meetingResponse.dues").description("Reservation meet dues"),
                                fieldWithPath("data.content[].meetingResponse.maxMember").description("Reservation meet max member"),
                                fieldWithPath("data.content[].meetingResponse.reservedMember").description("Reservation meet now member"),
                                fieldWithPath("data.content[].meetingResponse.meetingDate").description("Reservation meet date"),
                                fieldWithPath("data.content[].meetingResponse.regEmail").description("Reservation regEmail"),
                                fieldWithPath("data.pageable").description("pageable data")
                        )
                ));
    }

    @Test
    @DisplayName("예약을 생성한다. - 성공")
    void createReservationTest() throws Exception {
        // Given
        Account account = createAccount("createReservationSuccess@email.com");
        Meeting meeting = createMeeting(0);

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
                .andDo(document("create-reservation",
                        requestHeaders(
                            headerWithName("content-type").description("Content Type")
                        ),
                        requestFields(
                            fieldWithPath("meetingId").description("Meeting ID")
                        ),
                        responseHeaders(
                            headerWithName("content-type").description("Content Type")
                        ),
                        relaxedResponseFields(
                            fieldWithPath("statusCode").description("Status Code"),
                            fieldWithPath("message").description("response message"),
                            fieldWithPath("data.id").description("reservation ID")
                        )
                ));
    }

    @Test
    @DisplayName("예약을 생성한다. - 실패")
    void createReservationFailTest() throws Exception {
        // Given
        Optional<Account> getAccount = accountRepository.findByEmail("user@email.com");
        ReservationRequest request = ReservationRequest.builder()
                .meetingId(null)
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
    void deleteReservationTest() throws Exception {
        // Given
        Account account = this.createAccount("ReservationDelete@eamil.com");
        Meeting meeting = this.createMeeting(0);

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
                .andDo(document("delete-reservation",
                        requestHeaders(
                                headerWithName("content-type").description("Content Type")
                        ),
                        responseHeaders(
                                headerWithName("content-type").description("Content Type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("statusCode").description("Status Code"),
                                fieldWithPath("message").description("response message"),
                                fieldWithPath("data.id").description("reservation ID")
                        )
                ));

    }

    // TODO 수정 중
    @Test
    @DisplayName("동시성 테스트 - 예약 성공")
    void concurrentCreateTest() throws Exception {
        // Given
        Account account = createAccount("createReservationSuccess2@email.com");
        Meeting meeting = createMeeting(0);

        ReservationRequest reservation = ReservationRequest.builder()
                .meetingId(meeting.getId())
                .build();

        // When -> 요청이 100 번 동시 실행 될 경우
        int nThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch countDownLatch = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                try {
                    mockMvc.perform(post("/api/reservation")
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .content(objectMapper.writeValueAsString(reservation))
                                    .headers(getHeader(account))
                            )
                            .andDo(print())
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        // Then -> 실제 예약된 인원이 1명인지 확인
        ResultActions resultActions = mockMvc.perform(get("/api/meeting/{id}", meeting.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(getHeader(account)
                        ))
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);

        System.out.println("Meeting reservedMember :: " + jsonNode.get("data").get("reservedMember").asInt());
    }

    @Test
    @DisplayName("기존에 등록된 예약에 대한 예약 시 Exception 확인")
    void createDuplicationExceptionTest() {

    }

    @Test
    @DisplayName("동시성 테스트 - 예약 취소")
    @Disabled
    void concurrentDeleteTest() throws Exception {
        Account account = createAccount("concurrentDeleteTest@email.com");
        Meeting meeting = createMeeting(0);

        ReservationRequest reservation = ReservationRequest.builder()
                .meetingId(meeting.getId())
                .build();

        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reservation))
                        .headers(getHeader(account))
                )
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reservation))
                        .headers(getHeader(account))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Account createAccount(String email) {
        Account account = Account.builder()
                .email(email)
                .password("1234")
                .phoneNumber("010-1234-5678")
                .gender(Gender.getByValue("M"))
                .role(AccountRole.USER)
                .build();

        return accountRepository.save(account);
    }

    private Meeting createMeeting(int index) {
        Account getAccount = accountRepository.findByEmail("user@email.com")
                .orElseThrow(ExceptionType.ACCOUNT_NOT_FOUND::getException);

        Random random = new Random();
        Integer postalNum = 10000 + random.nextInt(60000); // 10000 ~ 59999

        Address address = Address.builder()
                .city("서울시 " + index)
                .state("구로구")
                .roadName("디지털로 " + index)
                .postalCode(postalNum.toString())
                .build();

        int placeNum = 1 + random.nextInt(3); // 1 ~ 3
        PlaceType placeType = PlaceType.getByValue(placeNum);

        Place place = Place.builder()
                .name("장소 이름 " + index)
                .placeType(placeType)
                .address(address)
                .build();
        Meeting meeting = Meeting.builder()
                .title("회의 제목" + index)
                .content("회의 내용" + index)
                .description("회의 설명" + index)
                .meetingType(MeetingType.ONLINE)
                .place(place)
                .dues(10000)
                .maxMember(1)
                .build();

        meeting.setAccount(getAccount);

        return meetingRepository.save(meeting);
    }

}