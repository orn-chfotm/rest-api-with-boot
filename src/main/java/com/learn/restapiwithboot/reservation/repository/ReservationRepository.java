package com.learn.restapiwithboot.reservation.repository;

import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.domain.embed.ReservationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsById(ReservationId id);

    void deleteById(ReservationId id);
}
