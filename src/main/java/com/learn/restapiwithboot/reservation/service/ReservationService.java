package com.learn.restapiwithboot.reservation.service;

import com.learn.restapiwithboot.account.domain.QAccount;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.core.exceptions.enums.ExceptionType;
import com.learn.restapiwithboot.core.exceptions.impl.ResourceNotFoundException;
import com.learn.restapiwithboot.core.query.QueryDslUtil;
import com.learn.restapiwithboot.meeting.domain.QMeeting;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import com.learn.restapiwithboot.reservation.domain.QReservation;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.dto.response.ReservationResponse;
import com.learn.restapiwithboot.reservation.mapper.ReservationMapper;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final AccountRepository accountRepository;

    private final MeetingRepository meetingRepository;

    private final ReservationMapper reservationMapper;

    private final ModelMapper modelMapper;

    private final JPAQueryFactory jpaQueryFactory;

    private final QueryDslUtil queryDslUtil;

    public Page<ReservationResponse> getReservation(Long accountId, Pageable pageable) {

        List<Reservation> reservationList = jpaQueryFactory.selectFrom(QReservation.reservation)
                .where(QReservation.reservation.accountId.eq(accountId))
                .leftJoin(QMeeting.meeting).on(QReservation.reservation.meetingId.eq(QMeeting.meeting.id))
                .leftJoin(QAccount.account).on(QReservation.reservation.accountId.eq(QAccount.account.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(queryDslUtil.orderSpecifiers(pageable, Reservation.class, "reservation"))
                .fetch();

        int size = jpaQueryFactory.selectFrom(QReservation.reservation)
                .where(QReservation.reservation.accountId.eq(accountId))
                .fetch().size();

        List<ReservationResponse> collect = reservationList.stream()
                .map(reservationMapper::reservationToReservationResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, size);
    }

    /* MapStruct 사용 전 코드
    private ReservationResponse converToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .accountResponse(this.convertToDto(reservation.getAccount(), AccountResponse.class))
                .meetingResponse(this.convertToDto(reservation.getMeeting(), MeetingResponse.class))
                .build();
    }

    private <D> D convertToDto(Object entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }*/

    @Transactional
    public ReservationResponse createReservation(Long accountId, ReservationRequest reservationRequest) {

        if (!accountRepository.existsById(accountId)) {
            throw ExceptionType.ACCOUNT_NOT_FOUND.getException();
        }
        if (!meetingRepository.existsById(reservationRequest.getMeetingId())) {
            throw ExceptionType.RESOURCE_RESERVATION_NOT_FOUND.getException();
        }

        Reservation reservation = reservationMapper.reservationRequestToReservation(reservationRequest);
        reservation.setAccountId(accountId);
        reservationRepository.save(reservation);

        return reservationMapper.reservationToReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse deleteReservation(Long accountId, Long ReservationId) {

        boolean isExistence = jpaQueryFactory.selectFrom(QReservation.reservation)
                .where(QReservation.reservation.id.eq(ReservationId)
                        .and(QReservation.reservation.accountId.eq(accountId)))
                .fetch().size() == 1;

        if (!isExistence) {
            throw ExceptionType.RESOURCE_RESERVATION_NOT_FOUND.getException();
        }

        reservationRepository.deleteById(ReservationId);

        return ReservationResponse.builder().id(ReservationId).build();
    }
}
