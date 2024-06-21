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

    @Autowired
    MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public List<Meeting> getAllMeeting() {
        return meetingRepository.findAll();
    }

    public MeetingResponseDto getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회의가 없습니다."));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false);

        return objectMapper.convertValue(meeting, MeetingResponseDto.class);
    }

    public Integer createMeeting(Meeting meeting) {
        Meeting saveMettring = meetingRepository.save(meeting);
        return saveMettring.getId().intValue();
    }
}
