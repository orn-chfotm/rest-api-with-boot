package com.learn.restapiwithboot.meeting.domain;

import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.enums.PlaceType;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTest {

    @Test
    @DisplayName("Meeting 객체 생성 테스트")
    void createMeeting() {
        // Given
        String title = "title";
        String content = "content";
        MeetingType meetingType = MeetingType.ONLINE;
        Address address = Address.builder()
                .roadName("roadName")
                .city("city")
                .state("state")
                .postalCode("postalCode")
                .build();
        Place place = Place.builder()
                .name("name")
                .palceType(PlaceType.CAFE)
                .build();
        Meeting meeting = Meeting.builder()
                .title(title)
                .content(content)
                .meetingType(meetingType)
                .address(address)
                .place(place)
                .build();

        // When
        // Then
        assertEquals(title, meeting.getTitle());
        assertEquals(content, meeting.getContent());
        assertEquals(meetingType, meeting.getMeetingType());
        assertEquals(address, meeting.getAddress());
        assertEquals(place, meeting.getPlace());
    }

}