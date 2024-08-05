package com.learn.restapiwithboot.meeting.dto.response;

import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeetingResponse {

    private Long id;
    private String title;
    private String content;
    private String description;
    private String regEmail;
    private Integer dues;
    private Integer maxMember;
    private Integer reservedMember;

    private LocalDateTime meetingDate;
    private Place place;
    private MeetingType meetingType;

    @Builder
    public MeetingResponse(Long id, String title, String content, String description, MeetingType meetingType, Place place, Integer dues, Integer maxMember, Integer reservedMember, LocalDateTime meetingDate, String regEmail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.meetingType = meetingType;
        this.place = place;
        this.dues = dues;
        this.maxMember = maxMember;
        this.reservedMember = reservedMember;
        this.meetingDate = meetingDate;
        this.regEmail = regEmail;
    }
}
