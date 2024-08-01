package com.learn.restapiwithboot.reservation.controller;

import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import com.learn.restapiwithboot.reservation.dto.request.ReservationRequest;
import com.learn.restapiwithboot.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<?> getAllReservation(@AuthenticationPrincipal String accountId,
                                               Pageable pageable) {
        return SuccessResponse.of(reservationService.getAllReservation(Long.parseLong(accountId), pageable));
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@AuthenticationPrincipal String accountId,
                                               @RequestBody @Valid ReservationRequest reservationRequest) {
        return SuccessResponse.of(reservationService.createReservation(Long.parseLong(accountId), reservationRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id,
                                               @AuthenticationPrincipal String accountId) {
        reservationService.deleteReservation(id, Long.parseLong(accountId));
        return SuccessResponse.of(null);
    }
}
