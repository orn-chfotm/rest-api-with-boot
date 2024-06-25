package com.learn.restapiwithboot.meeting.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponseDto;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    MeetingService(MeetingRepository meetingRepository,
                   ObjectMapper objectMapper) {
        this.meetingRepository = meetingRepository;
        this.objectMapper = objectMapper;
    }

    public List<Meeting> getAllMeeting() {
        return meetingRepository.findAll();
    }

    public MeetingResponseDto getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회의가 없습니다."));
        return objectMapper.convertValue(meeting, MeetingResponseDto.class);
    }

    public MeetingResponseDto createMeeting(Meeting meeting) {
        Meeting saveMettring = meetingRepository.save(meeting);
        return objectMapper.convertValue(saveMettring, MeetingResponseDto.class);
    }
}
