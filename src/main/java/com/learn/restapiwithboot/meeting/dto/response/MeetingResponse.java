package com.learn.restapiwithboot.meeting.dto.response;

import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MeetingResponse {

    private Long id;

    private String title;

    private String content;

    private String description;

    private MeetingType meetingType;

    private Place place;

    private Integer dues;

    private Integer maxMember;

    private Integer reservedMember;

    private LocalDateTime meetingDate;

    private String regEmail;

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
