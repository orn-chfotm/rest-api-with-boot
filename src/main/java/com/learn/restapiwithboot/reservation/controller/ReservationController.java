package com.learn.restapiwithboot.reservation.controller;

import com.learn.restapiwithboot.core.annotation.CurrentUser;
import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<?> getAllReservation(@CurrentUser String accountId,
                                               Pageable pageable) {
        return SuccessResponse.of(reservationService.getReservation(Long.parseLong(accountId), pageable));
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@CurrentUser String accountId,
                                               @RequestBody @Valid ReservationRequest reservationRequest) {
        return SuccessResponse.of(reservationService.createReservation(Long.parseLong(accountId), reservationRequest));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@CurrentUser String accountId,
                                               @PathVariable Long reservationId) {
        return SuccessResponse.of(reservationService.deleteReservation(Long.parseLong(accountId), reservationId));
    }
}
