package com.learn.restapiwithboot.reservation.domain.embed;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ReservationId implements Serializable {

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "meeting_id")
    private Long meetingId;

    @Builder
    public ReservationId(Long accountId, Long meetingId) {
        this.accountId = accountId;
        this.meetingId = meetingId;
    }
}
