package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.core.dto.response.SuccessResponse;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.service.MeetingService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMeeting(Pageable pageable) {
        return SuccessResponse.of(meetingService.getAllMeeting(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getMeeting(@PathVariable Long id) {
        return SuccessResponse.of(meetingService.getMeeting(id));
    }

    @PostMapping
    public ResponseEntity<?> createMeeting(Principal principal, @RequestBody @Valid MeetingRequest meetingRequest) {
        return SuccessResponse.of(meetingService.createMeeting(Long.parseLong(principal.getName()), meetingRequest));
    }

    @PutMapping()
    public ResponseEntity<?> updateMeeting(Principal principal, @RequestBody @Valid MeetingRequest meetingRequest) {
        return SuccessResponse.of(meetingService.updateMeeting(Long.parseLong(principal.getName()), meetingRequest));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteMeeting(Principal principal) {
        meetingService.deleteMeeting(Long.parseLong(principal.getName()));
        return SuccessResponse.of(null);
    }
}
