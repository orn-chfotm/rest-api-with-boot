package com.learn.restapiwithboot.meeting.service;

import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    private final ModelMapper modelMapper;

    @Autowired
    MeetingService(MeetingRepository meetingRepository,
                   ModelMapper modelMapper) {
        this.meetingRepository = meetingRepository;
        this.modelMapper = modelMapper;
    }

    public List<Meeting> getAllMeeting() {
        return meetingRepository.findAll();
    }

    public MeetingResponse getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회의가 없습니다."));
        return modelMapper.map(meeting, MeetingResponse.class);
    }

    public MeetingResponse createMeeting(Meeting meeting) {
        Meeting saveMettring = meetingRepository.save(meeting);
        return modelMapper.map(saveMettring, MeetingResponse.class);
    }
}
