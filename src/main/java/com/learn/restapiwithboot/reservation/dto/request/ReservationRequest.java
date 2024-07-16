package com.learn.restapiwithboot.reservation.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReservationRequest {

    @NotNull(message = "모임 ID가 필요합니다.")
    private Long meetingId;

    @Builder
    public ReservationRequest(Long meetingId) {
        this.meetingId = meetingId;
    }
}
