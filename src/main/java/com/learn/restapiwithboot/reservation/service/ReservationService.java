package com.learn.restapiwithboot.reservation.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.domain.QAccount;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.core.exceptions.exception.BaseException;
import com.learn.restapiwithboot.core.exceptions.enums.impl.AccountErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.ResourceErrorType;
import com.learn.restapiwithboot.core.query.QueryDslUtil;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.domain.QMeeting;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import com.learn.restapiwithboot.reservation.domain.QReservation;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.domain.embed.ReservationId;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.dto.response.ReservationResponse;
import com.learn.restapiwithboot.reservation.mapper.ReservationMapper;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
    private final JPAQueryFactory jpaQueryFactory;
    private final QueryDslUtil queryDslUtil;

    public Page<ReservationResponse> getAllReservation(Long accountId, Pageable pageable) {

        List<Reservation> reservationList = jpaQueryFactory.selectFrom(QReservation.reservation)
                .where(QReservation.reservation.account.id.eq(accountId))
                .leftJoin(QMeeting.meeting).on(QReservation.reservation.meeting.id.eq(QMeeting.meeting.id))
                .leftJoin(QAccount.account).on(QReservation.reservation.account.id.eq(QAccount.account.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(queryDslUtil.orderSpecifiers(pageable, Reservation.class, "reservation"))
                .fetch();

        int size = jpaQueryFactory.selectFrom(QReservation.reservation)
                .where(QReservation.reservation.account.id.eq(accountId))
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

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(AccountErrorType.ACCOUNT_NOT_FOUND));
        Meeting meeting = meetingRepository.findByIdWithLock(reservationRequest.getMeetingId())
                .orElseThrow(() -> new BaseException(ResourceErrorType.RESOURCE_MEETING_NOT_FOUND));

        ReservationId reservationId = ReservationId.builder()
                .accountId(account.getId())
                .meetingId(meeting.getId())
                .build();
        if (reservationRepository.existsById(reservationId)) {
            throw new BaseException(ResourceErrorType.APPLICANT_ALREADY);
        }

        meeting.increaseReservedMembers();
        meetingRepository.save(meeting);

        Reservation reservation = Reservation.builder()
                .account(account)
                .meeting(meeting)
                .build();

        reservation.setId(reservationId);
        reservationRepository.save(reservation);

        return reservationMapper.reservationToReservationResponse(reservation);
    }

    @Transactional
    public void deleteReservation(Long meetingId, Long accountId) {

        Meeting meeting = meetingRepository.findByIdWithLock(meetingId)
                .orElseThrow(() -> new BaseException(ResourceErrorType.RESOURCE_MEETING_NOT_FOUND));

        Reservation reservation = jpaQueryFactory.selectFrom(QReservation.reservation)
                .where(QReservation.reservation.meeting.id.eq(meetingId)
                        .and(QReservation.reservation.account.id.eq(accountId)))
                .fetchOne();

        if (reservation == null) {
            throw new BaseException(ResourceErrorType.RESOURCE_RESERVATION_NOT_FOUND);
        }

        meeting.decreaseReservedMembers();
        meetingRepository.save(meeting);

        ReservationId reservationId = ReservationId.builder()
                .accountId(accountId)
                .meetingId(meetingId)
                .build();

        reservationRepository.deleteById(reservationId);
    }
}
