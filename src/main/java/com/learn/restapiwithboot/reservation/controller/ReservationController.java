package com.learn.restapiwithboot.reservation.controller;

import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.service.ReservationService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<?> getAllReservation(Pageable pageable,
                                               Principal principal) {
        return SuccessResponse.of(reservationService.getReservation(principal.getName(), pageable));
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody @Valid ReservationRequest reservationRequest) {
        return SuccessResponse.of(reservationService.createReservation(reservationRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        return SuccessResponse.of(reservationService.deleteReservation(id));
    }
}
