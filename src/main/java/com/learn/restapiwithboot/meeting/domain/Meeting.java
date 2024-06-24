package com.learn.restapiwithboot.meeting.domain;

import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Meeting {

    @Id @GeneratedValue
    private Long id;

    @Comment("제목")
    @Column
    private String title;

    @Comment("내용")
    @Column
    private String content;

    @Comment("설명")
    @Column
    private String description;

    @Comment("모임 타입")
    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Comment("모임 장소")
    @Embedded
    private Place place;

    @Comment("모임 주소")
    @Embedded
    private Address address;

    @Builder
    public Meeting(String title, String content, String description, MeetingType meetingType, Place place, Address address) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.meetingType = meetingType;
        this.place = place;
        this.address = address;
    }

}
