package com.learn.restapiwithboot.reservation.dto.response;

import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import lombok.*;

@Setter @Getter
@NoArgsConstructor
public class ReservationResponse {

    private Long id;

    private AccountResponse accountResponse;

    private MeetingResponse meetingResponse;

    @Builder
    public ReservationResponse(Long id, AccountResponse accountResponse, MeetingResponse meetingResponse) {
        this.id = id;
        this.accountResponse = accountResponse;
        this.meetingResponse = meetingResponse;
    }
}
