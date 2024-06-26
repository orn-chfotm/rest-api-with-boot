package com.learn.restapiwithboot.meeting.dto.request;

import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class MeetingRequest {

    private String title;

    private String content;

    private String description;

    private MeetingType meetingType;

    private Place place;

    private Integer dues;

    @Builder
    public MeetingRequest(String title, String content, String description, MeetingType meetingType, Place place, Integer dues) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.meetingType = meetingType;
        this.place = place;
        this.dues = dues;
    }
}
