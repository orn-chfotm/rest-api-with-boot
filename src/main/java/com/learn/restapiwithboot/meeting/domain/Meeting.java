package com.learn.restapiwithboot.meeting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import com.learn.restapiwithboot.meeting.domain.embed.Address;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
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
    
    @Comment("모임 회비")
    @Column
    private Integer dues = 0;

    @Comment("모임 회비 여부")
    @Column
    private Boolean isDues = false;

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
    public Meeting(Long id, String title, String content, String description, Integer dues, Boolean isDues, MeetingType meetingType, Place place, Address address) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.dues = dues;
        this.isDues = isDues;
        this.meetingType = meetingType;
        this.place = place;
        this.address = address;
    }

    public void isPayDues() {
        if (this.dues == null) {
            this.isDues = false;
            this.dues = 0;
        } else {
            this.isDues = true;
        }
    }

}
