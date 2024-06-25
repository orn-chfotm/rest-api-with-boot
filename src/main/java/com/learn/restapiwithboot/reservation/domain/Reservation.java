package com.learn.restapiwithboot.reservation.domain;

import com.learn.restapiwithboot.meeting.domain.Meeting;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@Entity
public class Reservation {

    @Id @GeneratedValue
    private Integer id;

    @Comment("모임 신청 유저 ID")
    private String userId;

    @Comment("모임 ID")
    @Column(name = "meeting_id")
    private String meetingId;

    @OneToOne
    @JoinColumn(name = "meeting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Meeting meeting;

}
