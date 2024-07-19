package com.learn.restapiwithboot.meeting.service;

import com.learn.restapiwithboot.meeting.mapper.MeetingMapper;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    private final MeetingMapper meetingMapper;

    public Page<MeetingResponse> getAllMeeting(Pageable pageable) {
        Page<Meeting> allMeeting = meetingRepository.findAll(pageable);

        List<MeetingResponse> collect = allMeeting.getContent().stream()
                .map(meetingMapper::meetingToMeetingResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, allMeeting.getSize());
    }

    public MeetingResponse getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 모임이 없습니다."));

        return meetingMapper.meetingToMeetingResponse(meeting);
    }

    @Transactional
    public MeetingResponse createMeeting(long accountId, MeetingRequest meetingRequest) {
        Meeting meeting = meetingMapper.meetingReqeustToMeeting(meetingRequest);
        meeting.setRegId(accountId);

        Meeting saveMettring = meetingRepository.save(meeting);

        return meetingMapper.meetingToMeetingResponse(saveMettring);
    }

    @Transactional
    public MeetingResponse updateMeeting(Long id, Long meetingId, MeetingRequest meetingRequest) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 모임이 없습니다."));

        meetingMapper.updateMeetingFromRequest(meetingRequest, meeting);

        return meetingMapper.meetingToMeetingResponse(meeting);
    }

    @Transactional
    public void deleteMeeting(Long accountId) {
        if (!meetingRepository.existsById(accountId)) {
            throw new IllegalArgumentException("해당하는 모임이 없습니다.");
        }
        meetingRepository.deleteById(accountId);
    }
}
