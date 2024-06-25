package com.learn.restapiwithboot.meeting.dto.response;

import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class MeetingResponseDto {

    private Long id;

    private String title;

    private String content;

    private String description;

    private MeetingType meetingType;

    private Place place;

    private Address address;

    private Integer dues;

    private Boolean isDues;
}
