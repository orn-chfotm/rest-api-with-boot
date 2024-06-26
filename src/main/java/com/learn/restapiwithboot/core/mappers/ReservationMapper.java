package com.learn.restapiwithboot.core.mappers;

import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.dto.response.ReservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AccountMapper.class, MeetingMapper.class})
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "account", target = "accountResponseDto")
    @Mapping(source = "meeting", target = "meetingResponseDto")
    ReservationResponse reservationToReservationResponse(Reservation reservation);
}
