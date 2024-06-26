package com.learn.restapiwithboot.reservation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.dto.response.ReservationResponse;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final AccountRepository accountRepository;

    private final ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    public ReservationService(ReservationRepository reservationRepository,
                                AccountRepository accountRepository,
                                ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    public List<ReservationResponse> getReservation(String email) {

        Long accountId = accountRepository.findIdByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));

        List<Reservation> allByUserId = reservationRepository.findAllByAccountId(accountId);

        return allByUserId.stream()
                .map(this::converToResponse)
                .collect(Collectors.toList());
    }

    private ReservationResponse converToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .accountResponseDto(this.convertToDto(reservation.getAccount(), AccountResponse.class))
                .meetingResponseDto(this.convertToDto(reservation.getMeeting(), MeetingResponse.class))
                .build();
    }

    private <D> D convertToDto(Object entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }
}
