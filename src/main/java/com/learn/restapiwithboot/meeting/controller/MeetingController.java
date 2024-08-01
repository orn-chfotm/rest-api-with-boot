package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public ResponseEntity<?> getAllMeeting(Pageable pageable) {
        return SuccessResponse.of(meetingService.getAllMeeting(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getMeeting(@PathVariable Long id) {
        return SuccessResponse.of(meetingService.getMeeting(id));
    }

    @PostMapping
    public ResponseEntity<?> createMeeting(@AuthenticationPrincipal String accountId,
                                           @RequestBody @Valid MeetingRequest meetingRequest) {
        return SuccessResponse.of(meetingService.createMeeting(Long.parseLong(accountId), meetingRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id,
                                           @AuthenticationPrincipal String accountId,
                                           @RequestBody @Valid MeetingRequest meetingRequest) {
        return SuccessResponse.of(meetingService.updateMeeting(id, Long.parseLong(accountId), meetingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id,
                                           @AuthenticationPrincipal String accountId) {
        meetingService.deleteMeeting(id, Long.parseLong(accountId));
        return SuccessResponse.of(null);
    }
}
