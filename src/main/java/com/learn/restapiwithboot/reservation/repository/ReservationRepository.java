package com.learn.restapiwithboot.reservation.repository;

import com.learn.restapiwithboot.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByMeetingIdAndAccountId(Long meetingId, Long accountId);
}
