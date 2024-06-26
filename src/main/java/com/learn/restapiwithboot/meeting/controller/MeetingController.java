package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.common.dto.response.SuccessResponse;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public ResponseEntity<?> getAllMeeting() {
        return SuccessResponse.of(meetingService.getAllMeeting());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getMeeting(@PathVariable Long id) {
        return SuccessResponse.of(meetingService.getMeeting(id));
    }

    @PostMapping
    public ResponseEntity<?> createMeeting(@RequestBody @Valid MeetingRequest meetingRequest) {
        return SuccessResponse.of(meetingService.createMeeting(meetingRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id, @RequestBody @Valid MeetingRequest meetingRequest) {
        return SuccessResponse.of(meetingService.updateMeeting(id, meetingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long id) {
        return SuccessResponse.of(meetingService.deleteMeeting(id));
    }
}
