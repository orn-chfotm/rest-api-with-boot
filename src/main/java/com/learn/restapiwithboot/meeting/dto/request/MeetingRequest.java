package com.learn.restapiwithboot.meeting.dto.request;

import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;

@NoArgsConstructor
@Getter @Setter
public class MeetingRequest {
    private String title;

    private String content;

    private String description;

    private Integer dues;

    private MeetingType meetingType;

    private Address address;
}
