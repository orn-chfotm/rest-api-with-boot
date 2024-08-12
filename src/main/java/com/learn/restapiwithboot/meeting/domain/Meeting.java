package com.learn.restapiwithboot.meeting.domain;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import com.learn.restapiwithboot.core.exceptions.enums.impl.ResourceErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;
import com.learn.restapiwithboot.meeting.domain.embed.Place;
import com.learn.restapiwithboot.meeting.domain.enums.MeetingType;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@ToString
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

    @Comment("최대 인원")
    @Column
    private int maxMember;

    @Comment("예약된 인원")
    @Column
    private int reservedMember;

    @Comment("모임 날짜")
    private LocalDateTime meetingDate;

    @Comment("모임 타입")
    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Comment("모임 장소")
    @Embedded
    private Place place;

    @Comment("모임 등록자")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    public void increaseReservedMembers() {
        if (this.reservedMember >= maxMember) {
            throw new BaseException(ResourceErrorType.APPLICANT_MAX);
        }
        this.reservedMember++;
    }

    public void decreaseReservedMembers() {
        if (this.reservedMember <= 0) {
            throw new BaseException(ResourceErrorType.APPLICANT_MIN);
        }
        this.reservedMember--;
    }

    @Builder
    public Meeting(String title, String content, String description, int dues, int maxMember, int reservedMember, LocalDateTime meetingDate, MeetingType meetingType, Place place) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.dues = dues;
        this.maxMember = maxMember;
        this.reservedMember = reservedMember;
        this.meetingDate = meetingDate;
        this.meetingType = meetingType;
        this.place = place;
    }
}
