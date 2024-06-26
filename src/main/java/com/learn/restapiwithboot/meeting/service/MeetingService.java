package com.learn.restapiwithboot.meeting.service;

import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public MeetingResponse createMeeting(MeetingRequest meetingRequest) {
        Meeting meeting = modelMapper.map(meetingRequest, Meeting.class);
        Meeting saveMettring = meetingRepository.save(meeting);
        return modelMapper.map(saveMettring, MeetingResponse.class);
    }

    @Transactional
    public MeetingResponse updateMeeting(Long id, MeetingRequest meetingRequest) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회의가 없습니다."));

        modelMapper.map(meetingRequest, meeting);
        meeting.isPayDues();

        return modelMapper.map(meeting, MeetingResponse.class);
    }
}
