package com.learn.restapiwithboot.reservation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.restapiwithboot.reservation.domain.Reservation;
import com.learn.restapiwithboot.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ObjectMapper objectMapper;

    public ReservationService(ReservationRepository reservationRepository,
                              ObjectMapper objectMapper) {
        this.reservationRepository = reservationRepository;
        this.objectMapper = objectMapper;
    }

    public List<Reservation> getReservation(String userId) {
        return reservationRepository.findAllByUserId(userId);
    }
}
