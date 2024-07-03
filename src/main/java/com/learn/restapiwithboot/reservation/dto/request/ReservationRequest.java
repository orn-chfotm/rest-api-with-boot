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
    @NotNull(message = "email이 필요합니다.")
    private String email;

    @NotNull(message = "모임 ID가 필요합니다.")
    private Long meetingId;

    @Builder
    public ReservationRequest(String email, Long meetingId) {
        this.email = email;
        this.meetingId = meetingId;
    }
}
