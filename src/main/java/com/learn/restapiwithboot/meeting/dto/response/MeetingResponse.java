package com.learn.restapiwithboot.meeting.dto.response;

import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class MeetingResponse {

    private Long id;

    private String title;

    private String content;

    private String description;

    private MeetingType meetingType;

    private Place place;

    private Integer dues;

    private Boolean isDues;

    @Builder
    public MeetingResponse(Long id ) {
        this.id = id;
    }
}
