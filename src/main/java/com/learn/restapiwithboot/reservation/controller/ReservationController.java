package com.learn.restapiwithboot.reservation.controller;

import com.learn.restapiwithboot.common.dto.response.SuccessResponse;
import com.learn.restapiwithboot.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<?> getReservation(@RequestParam(name = "userId") String userId) {
        return SuccessResponse.of(reservationService.getReservation(userId));
    }
}
