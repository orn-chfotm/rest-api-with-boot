package com.learn.restapiwithboot.reservation.service;

import com.learn.restapiwithboot.account.dto.response.AccountResponse;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.core.exceptions.ResourceNotFoundException;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.mapper.ReservationMapper;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.dto.response.ReservationResponse;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.List.of;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final AccountRepository accountRepository;

    private final MeetingRepository meetingRepository;

    private final ReservationMapper reservationMapper;

    private final ModelMapper modelMapper;

    public ReservationService(ReservationRepository reservationRepository,
                                AccountRepository accountRepository,
                                MeetingRepository meetingRepository,
                                ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.accountRepository = accountRepository;
        this.meetingRepository = meetingRepository;
        this.reservationMapper = ReservationMapper.INSTANCE;
        this.modelMapper = modelMapper;
    }

    public List<ReservationResponse> getReservation(String email) {
        Long accountId = accountRepository.findIdByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));

        List<Reservation> allByUserId = reservationRepository.findAllByAccountId(accountId);

        return allByUserId.stream()
                .map(reservationMapper::reservationToReservationResponse)
                .collect(Collectors.toList());
    }

    private ReservationResponse converToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .accountResponse(this.convertToDto(reservation.getAccount(), AccountResponse.class))
                .meetingResponse(this.convertToDto(reservation.getMeeting(), MeetingResponse.class))
                .build();
    }

    private <D> D convertToDto(Object entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest reservationRequest) {

        Long accountId = accountRepository.findIdByEmail(reservationRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("해당하는 사용자가 없습니다."));

        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("해당하는 사용자가 없습니다.");
        }
        if (!meetingRepository.existsById(reservationRequest.getMeetingId())) {
            throw new ResourceNotFoundException("해당하는 회의가 없습니다.");
        }

        Reservation reservation = reservationMapper.reservationRequestToReservation(reservationRequest);
        reservation.setAccountId(accountId);
        reservationRepository.save(reservation);

        return reservationMapper.reservationToReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse deleteReservation(Long id) {
        if(!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("해당하는 회의가 없습니다.");
        }

        reservationRepository.deleteById(id);

        return ReservationResponse.builder().id(id).build();
    }
}
