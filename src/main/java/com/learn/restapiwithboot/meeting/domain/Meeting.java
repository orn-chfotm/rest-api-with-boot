package com.learn.restapiwithboot.meeting.domain;

import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class Meeting extends BaseTimeEntity {

    @Id
    @GeneratedValue
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
    private int dues;

    @Comment("모임 타입")
    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Comment("모임 장소")
    @Embedded
    private Place place;

    @Builder
    public Meeting(String title, String content, String description, Integer dues, MeetingType meetingType, Place place) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.dues = dues;
        this.meetingType = meetingType;
        this.place = place;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDues(Integer dues) {
        this.dues = dues;
    }

    public void setMeetingType(MeetingType meetingType) {
        this.meetingType = meetingType;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
