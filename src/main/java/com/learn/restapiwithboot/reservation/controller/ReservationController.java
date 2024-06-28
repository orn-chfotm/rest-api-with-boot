package com.learn.restapiwithboot.reservation.controller;

import com.learn.restapiwithboot.common.dto.response.SuccessResponse;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<?> getAllReservation(@RequestParam(name = "email") String email) {
        return SuccessResponse.of(reservationService.getReservation(email));
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody @Valid ReservationRequest reservationRequest) {
        return SuccessResponse.of(reservationService.createReservation(reservationRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@RequestParam Long id) {
        return SuccessResponse.of(reservationService.deleteReservation(id));
    }
}
