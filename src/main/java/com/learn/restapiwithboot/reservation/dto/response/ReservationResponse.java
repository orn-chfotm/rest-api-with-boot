package com.learn.restapiwithboot.reservation.dto.response;

import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import lombok.*;

@Setter @Getter
@NoArgsConstructor
public class ReservationResponse {

    private AccountResponse accountResponse;
    private MeetingResponse meetingResponse;

    @Builder
    public ReservationResponse(AccountResponse accountResponse, MeetingResponse meetingResponse) {
        this.accountResponse = accountResponse;
        this.meetingResponse = meetingResponse;
    }
}
