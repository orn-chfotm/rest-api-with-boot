package com.learn.restapiwithboot.meeting.domain;

import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @EqualsAndHashCode(of = "id")
@Entity
public class Meeting {

    @Id @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Embedded
    private Place place;

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
