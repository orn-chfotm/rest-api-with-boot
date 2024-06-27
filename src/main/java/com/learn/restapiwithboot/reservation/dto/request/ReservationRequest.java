package com.learn.restapiwithboot.reservation.dto.request;

import com.learn.restapiwithboot.meeting.domain.Meeting;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor
public class ReservationRequest {
    @NotNull(message = "유저 ID가 필요합니다.")
    private Long accountId;

    @NotNull(message = "모임 ID가 필요합니다.")
    private Long meetingId;

    @Builder
    public ReservationRequest(Long accountId, Long meetingId) {
        this.accountId = accountId;
        this.meetingId = meetingId;
    }
}
