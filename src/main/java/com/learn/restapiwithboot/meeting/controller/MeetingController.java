package com.learn.restapiwithboot.meeting.controller;

import com.learn.restapiwithboot.common.dto.response.SuccessResponse;
import com.learn.restapiwithboot.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
