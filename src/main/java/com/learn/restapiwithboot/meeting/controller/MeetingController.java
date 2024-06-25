package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.common.dto.response.SuccessResponse;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
        return SuccessResponse.of(meetingService.createMeeting(meeting));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@RequestBody MeetingRequest meetingRequest) {
        return SuccessResponse.of(meetingService.updateMeeting(meetingRequest));
    }
}
