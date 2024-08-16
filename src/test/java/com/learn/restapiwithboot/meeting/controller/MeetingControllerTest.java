package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.common.BaseTest;
import com.learn.restapiwithboot.config.token.JwtTokenProvider;
import com.learn.restapiwithboot.core.exceptions.enums.impl.AccountErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.ext.BasicException;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import com.learn.restapiwithboot.meeting.dto.request.AddressRequest;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.request.PlaceRequest;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    void GetAllMeetingTest() throws Exception {
        // Given
        Meeting meeting = createMeeting();

        mockMvc.perform(get("/api/meeting")
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getHeader(getToken()))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createDate,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-meeting-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type, charset=utf-8"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access-Token")
                        ),
                        relaxedResponseFields(
                            fieldWithPath("statusCode").description("상태 코드"),
                            fieldWithPath("message").description("Response Message"),
                            fieldWithPath("data.content[].id").description("Reservation Pk"),
                            fieldWithPath("data.content[].title").description("Reservation meeting title"),
                            fieldWithPath("data.content[].content").description("Reservation meeting content"),
                            fieldWithPath("data.content[].description").description("Reservation meeting description"),
                            fieldWithPath("data.content[].meetingType").description("Reservation meeting type"),
                            fieldWithPath("data.content[].place.name").description("Reservation meet place"),
                            fieldWithPath("data.content[].place.placeType").description("Reservation meet place type"),
                            fieldWithPath("data.content[].place.address").type(OBJECT).description("Reservation place address").optional(),
                            fieldWithPath("data.content[].place.address.roadName").type(STRING).description("Reservation meet place road name").optional(),
                            fieldWithPath("data.content[].place.address.city").type(STRING).description("Reservation meet place city name").optional(),
                            fieldWithPath("data.content[].place.address.state").type(STRING).description("Reservation meet place state").optional(),
                            fieldWithPath("data.content[].place.address.postalCode").type(STRING).description("Reservation meet place postal code").optional(),
                            fieldWithPath("data.content[].dues").description("Reservation meet pay due price"),
                            fieldWithPath("data.content[].maxMember").description("Reservation meet join max member"),
                            fieldWithPath("data.content[].reservedMember").description("Reservation meet now join member"),
                            //fieldWithPath("data.content[].meetingDate").description("Reservation meet Date"),
                            fieldWithPath("data.content[].regEmail").description("Reservation meet creater"),

                            fieldWithPath("data.pageable.sort.empty").description("Indicates if the sort is empty"),
                            fieldWithPath("data.pageable.sort.sorted").description("Indicates if the results are sorted"),
                            fieldWithPath("data.pageable.sort.unsorted").description("Indicates if the results are unsorted"),
                            fieldWithPath("data.pageable.offset").description("The offset of the current page"),
                            fieldWithPath("data.pageable.pageNumber").description("The number of the current page"),
                            fieldWithPath("data.pageable.pageSize").description("The size of the page"),
                            fieldWithPath("data.pageable.unpaged").description("Indicates if the pagination is disabled"),
                            fieldWithPath("data.pageable.paged").description("Indicates if the pagination is enabled"),
                            fieldWithPath("data.last").description("Indicates if this is the last page"),
                            fieldWithPath("data.totalElements").description("The total number of elements"),
                            fieldWithPath("data.totalPages").description("The total number of pages"),
                            fieldWithPath("data.size").description("The size of the current page"),
                            fieldWithPath("data.number").description("The number of the current page"),
                            fieldWithPath("data.sort.empty").description("Indicates if the sort is empty"),
                            fieldWithPath("data.sort.sorted").description("Indicates if the results are sorted"),
                            fieldWithPath("data.sort.unsorted").description("Indicates if the results are unsorted"),
                            fieldWithPath("data.first").description("Indicates if this is the first page"),
                            fieldWithPath("data.numberOfElements").description("The number of elements in the current page"),
                            fieldWithPath("data.empty").description("Indicates if the page is empty")
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

        return jwtTokenProvider.generateAccessToken(account);
    }

    @Test
    @DisplayName("특정 회의를 조회한다. - 성공 시")
    void getMeetingTest() throws Exception {
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
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content Type, charset=utf-8"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization JWT token")
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
                                fieldWithPath("data.place.address").type(OBJECT).description("Reservation place address").optional(),
                                fieldWithPath("data.place.address.roadName").type(STRING).description("Reservation meet place road name").optional(),
                                fieldWithPath("data.place.address.city").type(STRING).description("Reservation meet place city name").optional(),
                                fieldWithPath("data.place.address.state").type(STRING).description("Reservation meet place state").optional(),
                                fieldWithPath("data.place.address.postalCode").type(STRING).description("Reservation meet place postal code").optional(),
                                fieldWithPath("data.dues").description("Reservation pay due"),
                                fieldWithPath("data.maxMember").description("Reservation join max member"),
                                fieldWithPath("data.reservedMember").description("Reservation join now member"),
                                fieldWithPath("data.meetingDate").description("Reservation meet date"),
                                fieldWithPath("data.regEmail").description("Reservation meet creater")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("특정 회의를 조회한다. - 실패 시")
    void getMeetingFailTest() throws Exception {
        // Given
        Long id = Long.MAX_VALUE;
        // When && Then
        mockMvc.perform(get("/api/meeting/{id}", id)
                        .characterEncoding("utf-8")
                        .headers(getHeader(getToken()))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회의를 등록한다. - 성공 시")
    void createMeetingTest() throws Exception {
        // Given
        MeetingRequest meeting = createSuccessRequestMeeting();

        // When && Then
        mockMvc.perform(post("/api/meeting")
                        .characterEncoding("utf-8")
                        .content(this.objectMapper.writeValueAsString(meeting))
                        .headers(getHeader(getToken()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-meeting",
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
    void failCreateMeetingTest() throws Exception {
        // Given
        MeetingRequest meeting = createFailRequestMeeting();

        // When && Then
        mockMvc.perform(post("/api/meeting")
                        .characterEncoding("utf-8")
                        .content(this.objectMapper.writeValueAsString(meeting))
                        .headers(getHeader(getToken()))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("회의를 수정한다 - 성공 시")
    void updateMeetingTest() throws Exception {
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
                                fieldWithPath("data.place.address").type(OBJECT).description("Reservation place address").optional(),
                                fieldWithPath("data.place.address.roadName").type(STRING).description("Reservation meet place road name").optional(),
                                fieldWithPath("data.place.address.city").type(STRING).description("Reservation meet place city name").optional(),
                                fieldWithPath("data.place.address.state").type(STRING).description("Reservation meet place state").optional(),
                                fieldWithPath("data.place.address.postalCode").type(STRING).description("Reservation meet place postal code").optional(),
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
                .meetingDate(LocalDateTime.now().plusDays(1))
                .place(place)
                .build();

        Account regAccount = accountRepository.findByEmail("user@email.com")
                .orElseThrow(() -> new BasicException(AccountErrorType.ACCOUNT_NOT_FOUND));
        meeting.setAccount(regAccount);

        return meetingRepository.save(meeting);
    }
}