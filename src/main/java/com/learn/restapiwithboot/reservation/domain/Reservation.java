package com.learn.restapiwithboot.reservation.domain;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@ToString
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Comment("모임 신청 유저 ID")
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Comment("모임 ID")
    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Meeting meeting;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account account;

    @Builder
    public Reservation(Long accountId, Long meetingId) {
        this.accountId = accountId;
        this.meetingId = meetingId;
    }
}
