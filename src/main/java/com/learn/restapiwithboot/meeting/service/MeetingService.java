package com.learn.restapiwithboot.meeting.service;

import com.learn.restapiwithboot.meeting.mapper.MeetingMapper;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    private final MeetingMapper meetingMapper;

    MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
        this.meetingMapper = MeetingMapper.INSTANCE;
    }

    public List<MeetingResponse> getAllMeeting() {
        List<Meeting> allMeeting = meetingRepository.findAll();

        return allMeeting.stream()
                .map(meetingMapper::meetingToMeetingResponse)
                .collect(Collectors.toList());
    }

    public MeetingResponse getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회의가 없습니다."));

        return meetingMapper.meetingToMeetingResponse(meeting);
    }

    public MeetingResponse createMeeting(MeetingRequest meetingRequest) {
        Meeting meeting = meetingMapper.meetingReqeustToMeeting(meetingRequest);

        meeting.isPayDues();
        Meeting saveMettring = meetingRepository.save(meeting);

        return meetingMapper.meetingToMeetingResponse(saveMettring);
    }

    @Transactional
    public MeetingResponse updateMeeting(Long id, MeetingRequest meetingRequest) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회의가 없습니다."));

        meetingMapper.updateMeetingFromRequest(meetingRequest, meeting);
        meeting.isPayDues();

        return meetingMapper.meetingToMeetingResponse(meeting);
    }
}
