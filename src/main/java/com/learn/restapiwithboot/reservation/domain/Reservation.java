package com.learn.restapiwithboot.reservation.domain;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@ToString
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Comment("모임 신청 유저")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", referencedColumnName = "id")
    private Meeting meeting;

    @Comment("모임 정보")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Builder
    public Reservation(Account account, Meeting meeting) {
        this.account = account;
        this.meeting = meeting;
    }
}
