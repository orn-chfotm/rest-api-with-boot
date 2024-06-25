package com.learn.restapiwithboot.reservation.dto.response;

import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class ReservationResponse {

    private Long id;

    private AccountResponse accountResponseDto;

    private MeetingResponse meetingResponseDto;

    @Builder
    public ReservationResponse(Long id, AccountResponse accountResponseDto, MeetingResponse meetingResponseDto) {
        this.id = id;
        this.accountResponseDto = accountResponseDto;
        this.meetingResponseDto = meetingResponseDto;
    }
}
