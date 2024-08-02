package com.learn.restapiwithboot.reservation.domain;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.core.domain.BaseTimeEntity;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.reservation.domain.embed.ReservationId;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@ToString
public class Reservation extends BaseTimeEntity {

    @EmbeddedId
    private ReservationId id;

    @Comment("모임 신청 유저")
    @MapsId("meetingId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", referencedColumnName = "id")
    private Meeting meeting;

    @Comment("모임 정보")
    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    public void setId(ReservationId id) {
        this.id = id;
    }

    @Builder
    public Reservation(Account account, Meeting meeting) {
        this.account = account;
        this.meeting = meeting;
    }
}
