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

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MeetingControllerTest extends BaseTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders getHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
        return httpHeaders;
    }

    @Test
    @DisplayName("전체 회의 목록을 조회한다. - 성공 시")
    void testGetAllMeeting() throws Exception {
        // Given
        Meeting meeting = createMeeting();

        String token = getToken();

        mockMvc.perform(get("/api/meeting")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getHeader(token))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createDate,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-meetingList",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type"),
                                headerWithName("charset").description("Encoding"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access-Token")
                        ),
                        responseFields(
                            fieldWithPath("statusCode").description("상태 코드"),
                            fieldWithPath("message").description("Response Message"),
                            fieldWithPath("data[].id").description("Reservation Pk"),
                            fieldWithPath("data[].title").description("Reservation meeting title"),
                            fieldWithPath("data[].content").description("Reservation meeting content"),
                            fieldWithPath("data[].description").description("Reservation meeting description"),
                            fieldWithPath("data[].meetingType").description("Reservation meeting type"),
                            fieldWithPath("data[].place.name").description("Reservation meet place"),
                            fieldWithPath("data[].place.placeType").description("Reservation meet place type"),
                            fieldWithPath("data[].place.address.roadName").description("Reservation meet place load name"),
                            fieldWithPath("data[].place.address.city").description("Reservation meet place cityName"),
                            fieldWithPath("data[].place.address.state").description("Reservation meet Place state"),
                            fieldWithPath("data[].place.address.postalCode").description("Reservation meet place postalCode"),
                            fieldWithPath("data[].dues").description("Reservation meet pay due price"),
                            fieldWithPath("data[].maxMember").description("Reservation meet join max member"),
                            fieldWithPath("data[].reservedMember").description("Reservation meet now join member"),
                            fieldWithPath("data[].meetingDate").description("Reservation meet Date")
                        )
                    ));
    }

    /**
     * Get token
     */
    private String getToken() {
        Account account = accountRepository.findByEmail("user@email.com").orElseThrow(
                () -> new IllegalArgumentException("가입되지 않은 이메일입니다.")
        );

        return jwtTokenProvider.generateAsseccToken(account);
    }

    @Test
    @DisplayName("특정 회의를 조회한다. - 성공 시")
    void testGetMeeting() throws Exception {
        // Given
        Meeting meeting = createMeeting();
        Long id = meeting.getId();

        // When && Then
        mockMvc.perform(get("/api/meeting/{id}", id)
                        .characterEncoding("utf-8")
                        .headers(getHeader(getToken()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-meeting",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content Type"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization JWT token"),
                                headerWithName("charset").description("Char Encoding")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("response Status Code - Http Status Code"),
                                fieldWithPath("message").description("response detail Message"),
                                fieldWithPath("data[].id").description("Reservation Pk"),
                                fieldWithPath("data[].title").description("Reservation meet title"),
                                fieldWithPath("data[].content").description("Reservation meet content"),
                                fieldWithPath("data[].description").description("Reservation meet description"),
                                fieldWithPath("data[].meetingType").description("Reservation meet  Type"),
                                fieldWithPath("data[].place.name").description("Reservation place name"),
                                fieldWithPath("data[].place.placeType").description("Reservation place Type"),
                                fieldWithPath("data[].place.address.roadName").description("Reservation meet Address road name"),
                                fieldWithPath("data[].place.address.city").description("Reservation meet city"),
                                fieldWithPath("data[].place.address.state").description("Reservation meet city state"),
                                fieldWithPath("data[].place.address.postalCode").description("Reservation meet place postalCode"),
                                fieldWithPath("data[].dues").description("Reservation pay due"),
                                fieldWithPath("data[].maxMember").description("Reservation join max member"),
                                fieldWithPath("data[].reservedMember").description("Reservation join now member"),
                                fieldWithPath("data[].meetingDate").description("Reservation meet date")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("특정 회의를 조회한다. - 실패 시")
    void testExceptionGetMeeting() throws Exception {
        // Given
        Long id = 3L;
        // When && Then
        mockMvc.perform(get("/api/meeting/{id}", id)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회의를 등록한다. - 성공 시")
    void testCreateMeeting() throws Exception {
        // Given
        MeetingRequest meeting = createSuccessRequestMeeting();

        System.out.println(this.objectMapper.writeValueAsString(meeting));

        // When && Then
        mockMvc.perform(post("/api/meeting")
                        .characterEncoding("utf-8")
                        .content(this.objectMapper.writeValueAsString(meeting))
                        .headers(getHeader(getToken()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("insert-meeting",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content Type - application/json, charset=utf-8"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization JWT Access token")
                        ),
                        requestFields(
                                fieldWithPath("title").description("meeting title"),
                                fieldWithPath("content").description("meeting content"),
                                fieldWithPath("description").description("meeting description"),
                                fieldWithPath("dues").description("meeting pay due"),
                                fieldWithPath("meetingType").description("meeting type"),
                                fieldWithPath("place.name").description("meeting meet place name"),
                                fieldWithPath("place.placeType").description("meeting meet place type"),
                                fieldWithPath("place.address.roadName").description("meeting meet place load name"),
                                fieldWithPath("place.address.city").description("meeting meet place city name"),
                                fieldWithPath("place.address.state").description("meeting meet place state"),
                                fieldWithPath("place.address.postalCode").description("meeting meet place postalCode"),
                                fieldWithPath("maxMember").description("meeting join max member"),
                                fieldWithPath("meetingDate").description("meeting meet date")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                            fieldWithPath("statusCode").description("response Status Code - Http Status Code"),
                            fieldWithPath("message").description("response detail Message"),
                            fieldWithPath("data.id").description("Reservation Pk"),
                            fieldWithPath("data.title").description("Reservation meet title"),
                            fieldWithPath("data.content").description("Reservation meet content"),
                            fieldWithPath("data.description").description("Reservation meet description"),
                            fieldWithPath("data.meetingType").description("Reservation meet  Type"),
                            fieldWithPath("data.place.name").description("Reservation place name"),
                            fieldWithPath("data.place.placeType").description("Reservation place Type"),
                            fieldWithPath("data.place.address.roadName").description("Reservation meet Address road name"),
                            fieldWithPath("data.place.address.city").description("Reservation meet city"),
                            fieldWithPath("data.place.address.state").description("Reservation meet city state"),
                            fieldWithPath("data.place.address.postalCode").description("Reservation meet place postalCode"),
                            fieldWithPath("data.dues").description("Reservation pay due"),
                            fieldWithPath("data.maxMember").description("Reservation join max member"),
                            fieldWithPath("data.reservedMember").description("Reservation join now member"),
                            fieldWithPath("data.meetingDate").description("Reservation meet date"),
                            fieldWithPath("data.regEmail").description("Reservation registrant date")
                        )
                ));
    }

    @Test
    @DisplayName("회의를 등록한다. - 실패 시")
    void testFailCreateMeeting() throws Exception {
        // Given
        MeetingRequest meeting = createFailRequestMeeting();

        // When && Then
        mockMvc.perform(post("/api/meeting")
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
                .placeType("RESTAURANT")
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
                        .characterEncoding("utf-8")
                        .content(this.objectMapper.writeValueAsString(meetingRequest))
                        .headers(getHeader(getToken()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-meeting",
                    requestHeaders(
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("content type, char set"),
                            headerWithName(HttpHeaders.AUTHORIZATION).description("authorization JWT Access Token")
                    ),
                    requestFields(
                            fieldWithPath("title").description("update resource - meeting title"),
                            fieldWithPath("content").description("update resource - meeting content"),
                            fieldWithPath("description").description("update resource - meeting description"),
                            fieldWithPath("dues").description("update resource - meeting pay due"),
                            fieldWithPath("meetingType").description("update resource -  meeting type"),
                            fieldWithPath("place.name").description("update resource - meeting meet place name"),
                            fieldWithPath("place.placeType").description("update resource - meeting meet place type"),
                            fieldWithPath("place.address.roadName").description("update resource - meeting meet place load name"),
                            fieldWithPath("place.address.city").description("update resource - meeting meet place city name"),
                            fieldWithPath("place.address.state").description("update resource - meeting meet place state"),
                            fieldWithPath("place.address.postalCode").description("update resource - meeting meet place postalCode"),
                            fieldWithPath("maxMember").description("update resource - meeting join max member"),
                            fieldWithPath("meetingDate").description("update resource - meeting meet date")
                    ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").description("updated response - Status Code, Http Status Code"),
                                fieldWithPath("message").description("updated response - detail Message"),
                                fieldWithPath("data.id").description("updated response - Reservation Pk"),
                                fieldWithPath("data.title").description("updated response - Reservation meet title"),
                                fieldWithPath("data.content").description("updated response - Reservation meet content"),
                                fieldWithPath("data.description").description("updated response - Reservation meet description"),
                                fieldWithPath("data.meetingType").description("updated response - Reservation meet  Type"),
                                fieldWithPath("data.place.name").description("updated response - Reservation place name"),
                                fieldWithPath("data.place.placeType").description("updated response - Reservation place Type"),
                                fieldWithPath("data.place.address.roadName").description("updated response - Reservation meet Address road name"),
                                fieldWithPath("data.place.address.city").description("updated response - Reservation meet city"),
                                fieldWithPath("data.place.address.state").description("updated response - Reservation meet city state"),
                                fieldWithPath("data.place.address.postalCode").description("updated response - Reservation meet place postalCode"),
                                fieldWithPath("data.dues").description("updated response - Reservation pay due"),
                                fieldWithPath("data.maxMember").description("updated response - Reservation join max member"),
                                fieldWithPath("data.reservedMember").description("updated response - Reservation join now member"),
                                fieldWithPath("data.meetingDate").description("updated response - Reservation meet date"),
                                fieldWithPath("data.regEmail").description("Reservation registrant date")
                        )
                ))
        ;
    }

    /**
     * Test date For Success Test Module
     */
    private MeetingRequest createSuccessRequestMeeting() {
        PlaceRequest place = PlaceRequest.builder()
                .name("장소 이름")
                .placeType("CAFE")
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

    /**
     * Test date For Fail Test Module
     */
    private MeetingRequest createFailRequestMeeting() {
        PlaceRequest place = PlaceRequest.builder()
                .name("장소 이름")
                .placeType(PlaceType.CAFE.name())
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

    /**
     * Insert test date For test module
     */
    private Meeting createMeeting() {
        Place place = Place.builder()
                .name("장소 이름")
                .placeType(PlaceType.CAFE)
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