package com.learn.restapiwithboot.reservation.mapper;

import com.learn.restapiwithboot.account.mapper.AccountMapper;
import com.learn.restapiwithboot.meeting.mapper.MeetingMapper;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.dto.response.ReservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, MeetingMapper.class})
public interface ReservationMapper {

    @Mapping(source = "account", target = "accountResponse")
    @Mapping(source = "meeting", target = "meetingResponse")
    ReservationResponse reservationToReservationResponse(Reservation reservation);

}
